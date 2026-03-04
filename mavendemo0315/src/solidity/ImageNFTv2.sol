// SPDX-License-Identifier: MIT
pragma solidity ^0.6.10;
pragma experimental ABIEncoderV2; // 开启高级编码器，支持返回结构体

// ----------------------------------------------------------------------------
// SafeMath 库: 防止整数溢出
// ----------------------------------------------------------------------------
library SafeMath {
    function add(uint256 a, uint256 b) internal pure returns (uint256) {
        uint256 c = a + b;
        require(c >= a, "SafeMath: addition overflow");
        return c;
    }

    function sub(uint256 a, uint256 b) internal pure returns (uint256) {
        require(b <= a, "SafeMath: subtraction overflow");
        uint256 c = a - b;
        return c;
    }

    function mul(uint256 a, uint256 b) internal pure returns (uint256) {
        if (a == 0) return 0;
        uint256 c = a * b;
        require(c / a == b, "SafeMath: multiplication overflow");
        return c;
    }
}

// ----------------------------------------------------------------------------
// 主合约
// ----------------------------------------------------------------------------
contract ImageNFTv2 {
    using SafeMath for uint256; // 对 uint256 类型使用 SafeMath 方法

    // 基础数据结构
    struct NFT {
        uint256 id;           // 链上自增 Token ID
        string imageId;       // 链下业务系统图片ID (UUID)
        string fileHash;      // 文件SHA-256哈希 (核心确权凭证)
        string uri;           // 图片访问地址 (MinIO URL)
        string name;          // NFT名称
        string description;   // 描述
        uint256 price;        // 价格
        bool isForSale;       // 是否在售
        address owner;        // 所有者地址
        uint256 createTime;   // 铸造时间戳
    }

    // 状态变量
    mapping(uint256 => NFT) private nfts;           // tokenId => NFT详情
    mapping(address => uint256) private balances;   // 用户余额账本 (链上积分)
    uint256 private nextTokenId = 1;                // 自增ID计数器

    // 事件 (Java后端监听这些事件来同步数据库)
    event NFTMinted(address indexed owner, uint256 indexed tokenId, string imageId, string fileHash);
    event NFTSold(address indexed seller, address indexed buyer, uint256 indexed tokenId, uint256 price);
    event PriceUpdated(uint256 indexed tokenId, uint256 newPrice);
    event SaleStatusChanged(uint256 indexed tokenId, bool isForSale);
    event Withdraw(address indexed user, uint256 amount);
    event Transfer(address indexed from, address indexed to, uint256 indexed tokenId);

    // ==========================================
    // 1. 核心交易功能
    // ==========================================

    /**
     * @dev 铸造单个NFT (包含完整的元数据)
     */
    function mintNFT(
        string memory _imageId,
        string memory _fileHash,
        string memory _minioUrl,
        string memory _name,
        string memory _description,
        uint256 _price
    ) public returns (uint256) {
        uint256 tokenId = nextTokenId;
        nextTokenId = nextTokenId.add(1);

        nfts[tokenId] = NFT({
            id: tokenId,
            imageId: _imageId,
            fileHash: _fileHash,
            uri: _minioUrl,
            name: _name,
            description: _description,
            price: _price,
            isForSale: false, // 默认不上架
            owner: msg.sender,
            createTime: block.timestamp // 记录链上时间
        });

        emit NFTMinted(msg.sender, tokenId, _imageId, _fileHash);
        return tokenId;
    }

    /**
     * @dev 购买 NFT
     */
    function buyNFT(uint256 tokenId) public {
        NFT storage nft = nfts[tokenId];

        require(nft.id != 0, "NFT does not exist");
        require(nft.isForSale, "NFT is not for sale");
        require(nft.owner != msg.sender, "Cannot buy your own NFT");
        require(balances[msg.sender] >= nft.price, "Insufficient balance");

        address seller = nft.owner;
        uint256 price = nft.price;

        // 资金转移
        balances[msg.sender] = balances[msg.sender].sub(price);
        balances[seller] = balances[seller].add(price);

        // 所有权转移
        nft.owner = msg.sender;
        nft.isForSale = false; // 购买后自动下架

        emit NFTSold(seller, msg.sender, tokenId, price);
    }


    /**
     * @dev 转移 NFT (赠送，不涉及资金)
     */
    function transferNFT(address to, uint256 tokenId) public {
        require(nfts[tokenId].owner == msg.sender, "Not the owner");
        require(to != address(0), "Invalid address");

        nfts[tokenId].owner = to;
        nfts[tokenId].isForSale = false;

        emit Transfer(msg.sender, to, tokenId);
    }

    // ==========================================
    // 2. 资金管理
    // ==========================================

    /**
     * @dev 充值 (模拟环境使用，生产环境应由管理员调用)
     */
    function deposit(uint256 amount) public {
        balances[msg.sender] = balances[msg.sender].add(amount);
    }

    /**
     * @dev 提现 (触发事件，由后端处理法币打款)
     */
    function withdraw(uint256 amount) public {
        require(balances[msg.sender] >= amount, "Insufficient balance");
        balances[msg.sender] = balances[msg.sender].sub(amount);
        emit Withdraw(msg.sender, amount);
    }

    /**
     * @dev 查询余额
     */
    function getBalance() public view returns (uint256) {
        return balances[msg.sender];
    }

    // ==========================================
    // 3. 商品管理
    // ==========================================

    function updatePrice(uint256 tokenId, uint256 newPrice) public {
        require(nfts[tokenId].owner == msg.sender, "Not the owner");
        nfts[tokenId].price = newPrice;
        emit PriceUpdated(tokenId, newPrice);
    }

    function setForSale(uint256 tokenId, bool isForSale) public {
        require(nfts[tokenId].owner == msg.sender, "Not the owner");
        nfts[tokenId].isForSale = isForSale;
        emit SaleStatusChanged(tokenId, isForSale);
    }

    // ==========================================
    // 4. 批量与查询优化 (View)
    // ==========================================

    /**
     * @dev 获取单个NFT详情
     */
    function getNFT(uint256 tokenId) public view returns (NFT memory) {
        return nfts[tokenId];
    }

    /**
     * @dev 获取总发行量
     */
    function getTotalSupply() public view returns (uint256) {
        return nextTokenId.sub(1);
    }

    /**
     * @dev 分页获取所有在售的 NFT
     * 返回: (NFT对象数组, 实际的TokenID数组)
     */
    function getForSaleNFTs(uint256 page, uint256 size) public view returns (NFT[] memory) {
        uint256 total = nextTokenId.sub(1);
        uint256 count = 0;

        // 1. 计算在售总数 (注意：数据量极大时这里可能会耗尽Gas，建议在后端通过数据库查询筛选)
        for (uint256 i = 1; i <= total; i++) {
            if (nfts[i].isForSale) {
                count++;
            }
        }

        // 2. 计算分页范围
        uint256 start = (page.sub(1)).mul(size);
        uint256 end = start.add(size);
        if (end > count) end = count;

        if (start >= count) {
            return new NFT[](0); // 空结果
        }

        // 3. 填充结果
        NFT[] memory result = new NFT[](end.sub(start));
        uint256 index = 0;
        uint256 matchCount = 0;

        for (uint256 i = 1; i <= total; i++) {
            if (nfts[i].isForSale) {
                if (matchCount >= start && matchCount < end) {
                    result[index] = nfts[i];
                    index++;
                }
                matchCount++;
                if (matchCount >= end) break;
            }
        }
        return result;
    }

    /**
     * @dev 获取某个用户持有的所有NFT
     * (注意：全量遍历，仅适用于Demo或小规模数据。生产环境请在Java后端查询数据库)
     */
    function getOwnedNFTs(address user) public view returns (NFT[] memory) {
        uint256 total = nextTokenId.sub(1);
        uint256 count = 0;

        // 1. 统计数量
        for (uint256 i = 1; i <= total; i++) {
            if (nfts[i].owner == user) {
                count++;
            }
        }

        // 2. 填充数组
        NFT[] memory result = new NFT[](count);
        uint256 index = 0;
        for (uint256 i = 1; i <= total; i++) {
            if (nfts[i].owner == user) {
                result[index] = nfts[i];
                index++;
            }
        }
        return result;
    }
}