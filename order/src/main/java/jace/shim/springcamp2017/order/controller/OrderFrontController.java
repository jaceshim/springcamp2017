package jace.shim.springcamp2017.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jace.shim.springcamp2017.member.model.read.Member;
import jace.shim.springcamp2017.order.exception.InvalidRequestException;
import jace.shim.springcamp2017.order.exception.MemberNotSignedException;
import jace.shim.springcamp2017.order.infra.read.ProductReadRepository;
import jace.shim.springcamp2017.order.infra.remote.MemberInfoRepository;
import jace.shim.springcamp2017.order.model.command.OrderCommand;
import jace.shim.springcamp2017.order.model.read.Order;
import jace.shim.springcamp2017.product.model.read.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jaceshim on 2017. 3. 29..
 */
@Controller
@Slf4j
public class OrderFrontController {

	private static final String ITEM_DELIM = ":";

	private static DecimalFormat PRICE_DECIMAL_FORMAT = new DecimalFormat("#,##0");

	private static String ORDER_API = "http://order.jaceshim.com:20001/api/orders";

	@Autowired
	private ProductReadRepository productReadRepository;

	@Autowired
	private MemberInfoRepository memberInfoRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public String getProducts(Model model) {

		final List<Product> products = productReadRepository.findAll();
		model.addAttribute("products", products);

		return "products";
	}

	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	public String checkoutForm(@RequestParam(value = "checkoutItems[]", required = true) List<String> checkoutItems,
		@CookieValue(value = "SESSION") String sessionId, Model model) {

		if (sessionId == null || sessionId.isEmpty()) {
			throw new MemberNotSignedException("Member session not found!!");
		}

		final Member member = memberInfoRepository.findBySessionId(sessionId);
		log.debug("로그인 사용자 정보 : {}", member.getId());

		model.addAttribute("member", member);
		model.addAttribute("checkoutInfo", getCheckoutInfo(checkoutItems));

		return "checkout";
	}

	@RequestMapping(value = "/orders", method = RequestMethod.POST)
	public ResponseEntity<Order> createOrder(@RequestBody @Valid OrderCommand.CreateOrder orderCreateCommand,
		@CookieValue(value = "SESSION") String sessionId, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		HttpHeaders headers = new HttpHeaders();
		Charset utf8 = Charset.forName("UTF-8"); MediaType mediaType = new MediaType("application", "json", utf8);
		headers.setContentType(mediaType);

		final Member member = memberInfoRepository.findBySessionId(sessionId);
		log.debug("로그인 사용자 정보 : {}", member.getId());

		OrderCommand.CreateOrder params
			= new OrderCommand.CreateOrder(member.getId(), orderCreateCommand.getOrderItems(), orderCreateCommand.getDelivery());

		try {
			final RestTemplate restTemplate = new RestTemplate();
			String orderJson = objectMapper.writeValueAsString(params);
			HttpEntity<String> formEntity = new HttpEntity<>(orderJson, headers);
			ResponseEntity<Order> response = restTemplate.exchange(ORDER_API, HttpMethod.POST, formEntity, Order.class);
			return response;
		} catch (HttpClientErrorException | JsonProcessingException e) {
			log.error("Order fail : error message={}", e.getMessage());
			return new ResponseEntity<>(new Order(null), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private CheckoutInfo getCheckoutInfo(List<String> checkoutItems) {
		final List<CheckoutItem> checkoutItemList = parseCheckoutItems(checkoutItems);
		final long totalOrderPrice = checkoutItemList.stream().mapToLong(item -> item.getProductPrice()).sum();
		return new CheckoutInfo(checkoutItemList, totalOrderPrice);
	}

	private List<CheckoutItem> parseCheckoutItems(List<String> checkoutItems) {
		return checkoutItems.stream().filter(item -> item != null && !item.isEmpty()).map(item -> {
			final String[] itemAttrs = item.split(ITEM_DELIM);
			final Product product = productReadRepository.findByProductId(Long.parseLong(itemAttrs[0]));
			return new CheckoutItem(product, Integer.parseInt(itemAttrs[1]));
		}).collect(Collectors.toList());
	}

	@Getter
	@NoArgsConstructor
	static public class CheckoutInfo {
		List<CheckoutItem> checkoutItems;
		Long totalOrderPrice;

		public CheckoutInfo(List<CheckoutItem> checkoutItems, Long totalOrderPrice) {
			this.checkoutItems = checkoutItems;
			this.totalOrderPrice = totalOrderPrice;
		}

		public String getFormatTotalOrderPrice() {
			return PRICE_DECIMAL_FORMAT.format(this.getTotalOrderPrice());
		}
	}

	@Getter
	@NoArgsConstructor
	static public class CheckoutItem {
		private Product product;
		private int quantity;

		public CheckoutItem(Product product, int quantity) {
			this.product = product;
			this.quantity = quantity;
		}

		public long getProductPrice() {
			return this.getProduct().getPrice() * this.getQuantity();
		}

		public String getFormatProductPrice() {
			return PRICE_DECIMAL_FORMAT.format(this.getProductPrice());
		}
	}

}
