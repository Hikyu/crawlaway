package space.kyu.crawlaway;

import space.kyu.crawlaway.entity.Config;

public class AVSOTest {

	public static void main(String[] args) {
		AVSOProcessor avsoProcessor = new AVSOProcessor();
		Crawler crawler = Crawler.create(avsoProcessor);
		Config config = new Config();
		config.setReTryTime(3)
			  .setSleepTime(100)//ms
			  .setThreadNum(4)
			  .setTimeOut(3000)//ms
			  .setExitEmptyQueueTime(60)//s
			  .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.100 Safari/537.36")
//			  .addHeader("Cookie", "AD_wav_b_MD_B_728x90=6; AD_wav_b_MD_T_728x90=1; AD_jav_b_SM_B_728x90=6; AD_enterTime=1481553515; AD_jav_b_SM_T_728x90=0; AD_wav_b_SM_T_728x90=0; AD_wwwp_b_SM_T_728x90=0; AD_adst_b_SM_T_728x90=1; AD_javu_b_SM_B_728x90=5; AD_javu_b_MD_T_728x90=4; AD_jav_b_MD_T_728x90=1; AD_javu_b_MD_B_728x90=5; AD_clic_b_POPUNDER=2; AD_adca_b_SM_T_728x90=1; AD_javu_b_SM_T_728x90=1; AD_wav_b_SM_B_728x90=10; AD_popa_b_POPUNDER=2; _ga=GA1.2.513180103.1481378512; _gat=1")
		      .addUrl(AVSOProcessor.startUrl);
		crawler.setCrawlConfig(config);
//		crawler.addPipeline(new ConsolePipeline());
		crawler.run();
		
	}
	
}


