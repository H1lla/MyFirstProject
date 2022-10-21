package springwebapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springwebapp.entites.Order;
import springwebapp.services.OrderService;
import springwebapp.utils.ShoppingCart;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping("/cart")
public class CartController {
    private ShoppingCart cart;

    private OrderService orderService;

    @Autowired
    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public String showCart(Model model) {
        model.addAttribute("items", cart.getItems());
        return "cart";
    }
    @GetMapping("/add/{id}")
    public String addToCart(Model model, @PathVariable("id")Long id) {
        cart.addProductById(id);
        return "redirect:/shop";
    }
    @GetMapping("/create_order")
    public String createOrder(Principal principal) {
        Order order = new Order();
        order.setItems(new ArrayList<>());
        order.setUsername(principal.getName());
        cart.getItems().stream().forEach(i -> {
                    order.getItems().add(i);
                    i.setOrder(order);
                });
        cart.getItems().clear();
        orderService.saveOrder(order);
        return "redirect:/shop";
    }
}
