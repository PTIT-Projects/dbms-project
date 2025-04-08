package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.Notification;
import vn.ptit.hrms.service.NotificationService;
import vn.ptit.hrms.service.EmployeeService;

@Controller
@RequestMapping("/admin/pages/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final EmployeeService employeeService;

    public NotificationController(NotificationService notificationService, EmployeeService employeeService) {
        this.notificationService = notificationService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllNotifications(Model model) {
        model.addAttribute("notifications", notificationService.getAllNotifications());
        return "pages/notification/list";
    }

    @GetMapping("/{id}")
    public String getNotificationById(@PathVariable Integer id, Model model) {
        model.addAttribute("notification", notificationService.getNotificationById(id));
        return "pages/notification/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("notification", new Notification());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/notification/create";
    }

    @PostMapping
    public String createNotification(@ModelAttribute Notification notification) {
        notificationService.createNotification(notification);
        return "redirect:/admin/pages/notifications";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("notification", notificationService.getNotificationById(id));
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/notification/edit";
    }

    @PostMapping("/{id}")
    public String updateNotification(@PathVariable Integer id, @ModelAttribute Notification notification) {
        notification.setId(id);
        notificationService.updateNotification(notification);
        return "redirect:/admin/pages/notifications";
    }

    @GetMapping("/{id}/delete")
    public String deleteNotification(@PathVariable Integer id) {
        notificationService.deleteNotification(id);
        return "redirect:/admin/pages/notifications";
    }
}