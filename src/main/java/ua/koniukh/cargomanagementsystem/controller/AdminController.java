package ua.koniukh.cargomanagementsystem.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.koniukh.cargomanagementsystem.model.Order;
import ua.koniukh.cargomanagementsystem.model.dto.OrderDTO;
import ua.koniukh.cargomanagementsystem.service.InvoiceService;
import ua.koniukh.cargomanagementsystem.service.OrderService;

@Controller
public class AdminController {

  @Autowired
  public AdminController(OrderService orderService, InvoiceService invoiceService) {
    this.orderService = orderService;
    this.invoiceService = invoiceService;
  }

  private final OrderService orderService;
  private final InvoiceService invoiceService;

  @GetMapping("/applications")
  public String getAllNotProcessedOrders(Model model) {
    List<Order> allOrderListFromDB = orderService.findAll();
    List<Order> OrderListFromDB =
        allOrderListFromDB
            .stream()
            .filter(order -> !order.isProcessed())
            .collect(Collectors.toList());
    List<Order> ProcessedOrderListFromDB =
        allOrderListFromDB.stream().filter(Order::isProcessed).collect(Collectors.toList());
    model.addAttribute("NotProcessedOrders", OrderListFromDB);
    model.addAttribute("ProcessedOrders", ProcessedOrderListFromDB);
    return "orders_processed_page";
  }

  @PostMapping("/applications/{id}/processed")
  public String processed(@PathVariable(value = "id") Long id, Model model) {
    Order order = orderService.findById(id);
    // todo mb merge
    invoiceService.createInvoice(order);
    orderService.processedOrder(id);
    return "redirect:/applications";
  }

  @PostMapping("/applications/{id}/delete")
  public String deleteUserOrder(@PathVariable(value = "id") Long id, Model model) {
    //        orderService.deleteById(id);
    return "redirect:/applications";
  }

  @GetMapping("/filter_page")
  public String filterPage(OrderDTO orderDTO, Model model) {
    List<Order> orderList = orderService.findAll();
    model.addAttribute("orders", orderList);
    return "filter_page";
  }

  @PostMapping("/filter_page")
  public String filter(OrderDTO orderDTO, Model model) {
    List<Order> orderList = orderService.findAll();
    if (orderDTO.isPaid()) {
      List<Order> OrderListFromDB =
          orderList
              .stream()
              .filter(Order::isOrderPaid)
              .filter(order -> order.getRouteFrom().equals(orderDTO.getRouteFrom()))
              .filter(order -> order.getRouteTo().equals(orderDTO.getRouteTo()))
              .collect(Collectors.toList());
      model.addAttribute("orders", OrderListFromDB);
      return "filter_page";
    } else if (orderDTO.isUnPaidForFilter()) {
      List<Order> OrderListFromDB =
          orderList
              .stream()
              .filter(order -> !order.isOrderPaid())
              .filter(order -> order.getRouteFrom().equals(orderDTO.getRouteFrom()))
              .filter(order -> order.getRouteTo().equals(orderDTO.getRouteTo()))
              .collect(Collectors.toList());
      model.addAttribute("orders", OrderListFromDB);
      return "filter_page";
    }
    List<Order> OrderListFromDB =
        orderList
            .stream()
            .filter(order -> order.getRouteFrom().equals(orderDTO.getRouteFrom()))
            .filter(order -> order.getRouteTo().equals(orderDTO.getRouteTo()))
            .collect(Collectors.toList());
    model.addAttribute("orders", OrderListFromDB);
    return "filter_page";
  }
}
