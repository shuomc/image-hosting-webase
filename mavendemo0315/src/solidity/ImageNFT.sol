// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract ImageNFT {
    // 基础数据结构
    struct NFT {
        string uri;           // NFT的URI (格式: 'nft:' + minio_url)
        string description;   // NFT描述
        uint256 price;        // 价格
        bool isForSale;       // 是否在售
        address owner;        // 所有者地址
    }

    // 状态变量
    mapping(uint256 => NFT) private nfts;           // tokenId => NFT
    mapping(address => uint256) private balances;   // 用户余额
    uint256 private nextTokenId = 1;                // 下一个tokenId

    // 事件
    event NFTMinted(address indexed owner, uint256 indexed tokenId, string uri);
    event NFTSold(address indexed seller, address indexed buyer, uint256 indexed tokenId, uint256 price);
    event PriceUpdated(uint256 indexed tokenId, uint256 newPrice);
    event SaleStatusChanged(uint256 indexed tokenId, bool isForSale);

    // 铸造NFT
    function mintNFT(string memory minioUrl, string memory description, uint256 price) public returns (uint256) {
        uint256 tokenId = nextTokenId++;
        string memory uri = string(abi.encodePacked("nft:", minioUrl));
        
        nfts[tokenId] = NFT({
            uri: uri,
            description: description,
            price: price,
            isForSale: false,
            owner: msg.sender
        });

        emit NFTMinted(msg.sender, tokenId, uri);
        return tokenId;
    }

    // 充值功能 - 简化为直接修改余额
    function deposit(uint256 amount) public {
        balances[msg.sender] += amount;
    }

    // 查询余额
    function getBalance() public view returns (uint256) {
        return balances[msg.sender];
    }

    // 更新NFT价格
    function updatePrice(uint256 tokenId, uint256 newPrice) public {
        require(nfts[tokenId].owner == msg.sender, "Not the owner");
        nfts[tokenId].price = newPrice;
        emit PriceUpdated(tokenId, newPrice);
    }

    // 设置NFT是否在售
    function setForSale(uint256 tokenId, bool isForSale) public {
        require(nfts[tokenId].owner == msg.sender, "Not the owner");
        nfts[tokenId].isForSale = isForSale;
        emit SaleStatusChanged(tokenId, isForSale);
    }

    // 购买NFT
    function buyNFT(uint256 tokenId) public {
        require(nfts[tokenId].isForSale, "NFT is not for sale");
        require(balances[msg.sender] >= nfts[tokenId].price, "Insufficient balance");
        require(nfts[tokenId].owner != msg.sender, "Cannot buy your own NFT");

        address seller = nfts[tokenId].owner;
        
        // 转移资金
        balances[seller] += nfts[tokenId].price;
        balances[msg.sender] -= nfts[tokenId].price;
        
        // 转移所有权
        nfts[tokenId].owner = msg.sender;
        nfts[tokenId].isForSale = false;
        
        emit NFTSold(seller, msg.sender, tokenId, nfts[tokenId].price);
    }

    // 获取NFT信息
    function getNFT(uint256 tokenId) public view returns (
        string memory uri,
        string memory description,
        uint256 price,
        bool isForSale,
        address owner
    ) {
        NFT memory nft = nfts[tokenId];
        return (nft.uri, nft.description, nft.price, nft.isForSale, nft.owner);
    }

    // 获取用户拥有的所有NFT
    function getOwnedNFTs(address owner) public view returns (uint256[] memory) {
        uint256 count = 0;
        for (uint256 i = 1; i < nextTokenId; i++) {
            if (nfts[i].owner == owner) {
                count++;
            }
        }

        uint256[] memory ownedTokens = new uint256[](count);
        uint256 index = 0;
        for (uint256 i = 1; i < nextTokenId; i++) {
            if (nfts[i].owner == owner) {
                ownedTokens[index] = i;
                index++;
            }
        }
        return ownedTokens;
    }
} 