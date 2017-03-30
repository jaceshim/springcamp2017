package jace.shim.springcamp2017.member.controller;

import jace.shim.springcamp2017.member.infra.read.MemberReadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by jaceshim on 2017. 3. 30..
 */
@Controller
public class MemberFrontController {

	@Autowired
	private MemberReadRepository memberReadRepository;

	@RequestMapping(value = "/signup", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String signup(Model model) {
		return "signup";
	}

}
