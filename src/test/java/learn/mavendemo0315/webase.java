package learn.mavendemo0315;

import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.config.Config;
import org.fisco.bcos.sdk.config.ConfigOption;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shuomc
 * @Date 2025/4/29
 * @Description
 */
@SpringBootTest
@ImportAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class})
@TestPropertySource(properties = {
        "spring.datasource.url=",
        "spring.datasource.driver-class-name=",
        "spring.datasource.username=",
        "spring.datasource.password="
})
public class webase {
    public final String configFile = "src/main/resources/config-example.toml";

    @Test
    public void test() throws Exception {
        // 初始化BcosSDK对象
        ConfigOption configOption = Config.load(configFile);
        BcosSDK sdk = new BcosSDK(configOption);
        // 获取Client对象，此处传入的群组ID为1
        Client client = sdk.getClient(1);
        // 构造AssembleTransactionProcessor对象，需要传入client对象，CryptoKeyPair对象和abi、binary文件存放的路径。abi和binary文件需要在上一步复制到定义的文件夹中。
        CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
        AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(client, keyPair, "src/main/resources/abi/", "src/main/resources/bin/");

        // 创建调用交易函数的参数，此处为传入一个参数
        List<Object> params = new ArrayList<>();
        params.add("1");
        params.add("10");
        // 调用HelloWorld合约，合约地址为helloWorldAddress， 调用函数名为『get』，函数参数类型为params
        TransactionResponse transactionResponse = transactionProcessor.sendTransactionAndGetResponseByContractLoader("Grade", "0x7b9558d415a181859c0467653bc27cbb7b8799f6", "addGrade", params);
        List<Object> returnValues = transactionResponse.getReturnObject();
        if (returnValues != null) {
            for (Object returnValue : returnValues) {
                System.out.println("上链返回值: " + returnValue.toString());
            }
        }

//        // 创建调用交易函数的参数，此处为传入一个参数
//        List<Object> params = new ArrayList<>();
//        params.add("nihao");
//        // 调用HelloWorld合约，合约地址为helloWorldAddress， 调用函数名为『set』，函数参数类型为params
//        TransactionResponse transactionResponse = transactionProcessor.sendTransactionAndGetResponseByContractLoader("HelloWorld", "0xbbb713d3b3270e4d4c0858d7392bc842907aea6d", "set", params);
//        List<Object> returnValues = transactionResponse.getReturnObject();
//        if (returnValues != null) {
//            for (Object returnValue : returnValues) {
//                System.out.println("上链返回值: " + returnValue.toString());
//            }
//        }
    }
}
