package com.scott.eshop.cache.controller;

import com.scott.eshop.cache.hystrix.GetBrandNameCommand;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rx.Observable;
import rx.Observer;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixObservableCommand;
import com.scott.eshop.cache.utils.HttpClientUtils;
import com.scott.eshop.cache.hystrix.GetCityNameCommand;
import com.scott.eshop.cache.hystrix.GetProductInfoCommand;
import com.scott.eshop.cache.hystrix.GetProductInfosCommand;
import com.scott.eshop.cache.model.ProductInfo;

/**
 * 缓存服务的接口
 * @author Administrator
 *
 */
@Controller
public class CacheController {

	@RequestMapping("/change/product")
	@ResponseBody
	public String changeProduct(Long productId) {
		// 拿到一个商品id
		// 调用商品服务的接口，获取商品id对应的商品的最新数据
		// 用HttpClient去调用商品服务的http接口
		String url = "http://127.0.0.1:8082/getProductInfo?productId=" + productId;
		String response = HttpClientUtils.sendGetRequest(url);
		System.out.println(response);  
		
		return "success";
	}
	
	/**
	 * nginx开始，各级缓存都失效了，nginx发送很多的请求直接到缓存服务要求拉取最原始的数据
	 * @param productId
	 * @return
	 */
	@RequestMapping("/getProductInfo")
	@ResponseBody
	public String getProductInfo(Long productId) {
		// 拿到一个商品id
		// 调用商品服务的接口，获取商品id对应的商品的最新数据
		// 用HttpClient去调用商品服务的http接口
		HystrixCommand<ProductInfo> getProductInfoCommand = new GetProductInfoCommand(productId);
		ProductInfo productInfo = getProductInfoCommand.execute();
		
		Long cityId = productInfo.getCityId();
		GetCityNameCommand getCityNameCommand = new GetCityNameCommand(cityId);
		String cityName = getCityNameCommand.execute();
		productInfo.setCityName(cityName);

		Long brandId = productInfo.getBrandId();
		GetBrandNameCommand getBrandNameCommand = new GetBrandNameCommand(brandId);
		String brandName = getBrandNameCommand.execute();
		productInfo.setBrandName(brandName);
		
//		Future<ProductInfo> future = getProductInfoCommand.queue();
//		try {
//			Thread.sleep(1000); 
//			System.out.println(future.get());  
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		System.out.println(productInfo);  
		return "success";
	}
	
	/**
	 * 一次性批量查询多条商品数据的请求
	 */
	@RequestMapping("/getProductInfos")
	@ResponseBody
	public String getProductInfos(String productIds) {
//		HystrixObservableCommand<ProductInfo> getProductInfosCommand =
//				new GetProductInfosCommand(productIds.split(","));
//		Observable<ProductInfo> observable = getProductInfosCommand.observe();
//
////		observable = getProductInfosCommand.toObservable(); // 还没有执行
//
//		observable.subscribe(new Observer<ProductInfo>() { // 等到调用subscribe然后才会执行
//
//			public void onCompleted() {
//				System.out.println("获取完了所有的商品数据");
//			}
//
//			public void onError(Throwable e) {
//				e.printStackTrace();
//			}
//
//			public void onNext(ProductInfo productInfo) {
//				System.out.println(productInfo);
//			}
//
//		});

		for (String productId : productIds.split(",")) {
			GetProductInfoCommand getProductInfoCommand = new GetProductInfoCommand(Long.valueOf(productId));

			ProductInfo productInfo = getProductInfoCommand.execute();

			System.out.println(productId);
			System.out.println(getProductInfoCommand.isResponseFromCache());
		}

		return "success";
	}
	
}
