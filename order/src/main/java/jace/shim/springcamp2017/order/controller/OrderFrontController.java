package jace.shim.springcamp2017.order.controller;

import jace.shim.springcamp2017.order.infra.read.ProductReadRepository;
import jace.shim.springcamp2017.order.model.read.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by jaceshim on 2017. 3. 29..
 */
@Controller
public class OrderFrontController {

	@Autowired
	private ProductReadRepository productReadRepository;

	@RequestMapping(value = "/products", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getProducts(Model model) {

		final List<Product> products = productReadRepository.findAll();
		model.addAttribute("products", products);

		return "products";
	}

}
