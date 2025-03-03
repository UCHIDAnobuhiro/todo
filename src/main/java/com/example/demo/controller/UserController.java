package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	//ログイン画面でユーザーデータの入力を行う
	@GetMapping("/login")
	public String loginSetUser(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "login/login";
	}

	//新規登録画面でユーザーデータの入力を請求
	@GetMapping("/createAccount")
	public String showCreateAccount(Model model) {
		User user = new User();
		model.addAttribute(user);
		return "login/createAccount";
	}

	//Sign up ボタンを押下時の動作
	@PostMapping("/createAccout")
	public String doCreateAccount(User user, Model model) {
		List<User> users = userRepository.findAll();

		boolean hasError = false;
		//passwordとconfirmPassword一致するかどうかをチェック
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			System.out.println("Password doesnt match");
			System.out.println(user.getPassword() + user.getConfirmPassword());
			model.addAttribute("errorMessage_pw", "Password doesn't match");
			hasError = true;
		}
		//nameは3文字以上であるかをチェック
		if (user.getName() == null || user.getName().length() <= 2) {
			model.addAttribute("errorMessage_name", "Name must be longer than 2 characters");
			hasError = true;
		}

		//既存アカウントであるかをチェック
		for (User existingUser : users) {
			if (existingUser.getEmail().equals(user.getEmail())) {
				System.out.println("Almost exist accout please try another");
				model.addAttribute("errorMessage_email", "Almost exist email please try another");
				hasError = true;
				break;
			}
		}
		if (hasError) {
			return "login/createAccount";
		}

		//データベースにデータを登録しログイン画面に戻る
		userService.saveUser(user);
		return "redirect:/login";
	}

	//ログイン画面からタスク一覧画面へ移動時の処理
	@PostMapping("todo/show")
	public String doCheckUserData(@ModelAttribute User user, Model model, HttpSession session) {

		//データベースでuserをチェックし、あるなら一覧画面へ移動、ないならエラーメッセージの表示
		model.addAttribute(user);
		List<User> users = userRepository.findAll();

		for (User existingUser : users) {
			if (existingUser.getEmail().equals(user.getEmail())
					&& existingUser.getPassword().equals(user.getPassword())) {
				//ユーザーIDとNAMEをsessionに保存し、タスク関連画面に利用される
				//ログアウトのときは削除する必要がある
				session.setAttribute("userId", existingUser.getId());
				session.setAttribute("userName", existingUser.getName());
				return "redirect:/todo/show";
			}
		}
		model.addAttribute("errorMessage_login", "Email or Password is incorrect");
		return "/login/login";
	}
}
