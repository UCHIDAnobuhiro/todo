package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String showLogin() {
		return "login";
	}

	@GetMapping("/")
	public String setUser(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "login";
	}

	@PostMapping("/show")
	public String requestMethodName(@ModelAttribute User user, Model model) {
		//データベースでuserをチェックし、あるなら/showを表示する、ないならloginに戻る
		List<User> users = userRepository.findAll();
		model.addAttribute(user);

		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getEmail().equals(user.getEmail())
					&& users.get(i).getPassword().equals(user.getPassword())) {
				System.out.println("ID:" + users.get(i).getEmail() + "PW:" + users.get(i).getPassword());
				return "show";
			}
		}
		System.out.println(user.getEmail());
		return "login";

	}

}
