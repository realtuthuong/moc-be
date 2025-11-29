//package com.example.MocBE.util;
//
//import com.example.MocBE.enums.*;
//import com.example.MocBE.model.*;
//import com.example.MocBE.repository.*;
//import com.example.MocBE.repository.LocationImagesRepository;
//import com.example.MocBE.repository.MenuItemImagesRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//
//@Component
//@RequiredArgsConstructor
//public class DataSeeder implements CommandLineRunner {
//
//    private final AccountRepository accountRepository;
//    private final RoleRepository roleRepository;
//    private final CategoryIngredientRepository categoryIngredientRepository;
//    private final CategoryMenuItemRepository categoryMenuItemRepository;
//    private final CustomerRepository customerRepository;
//    private final LocationImagesRepository locationImagesRepository;
//    private final MenuItemImagesRepository menuItemImagesRepository;
//    private final IngredientRepository ingredientRepository;
//    private final InvoiceRepository invoiceRepository;
//    private final LocationRepository locationRepository;
//    private final MenuItemRepository menuItemRepository;
//    private final MenuItemIngredientRepository menuItemIngredientRepository;
//    private final OrderRepository orderRepository;
//    private final OrderDetailRepository orderDetailRepository;
//    private final TableRepository tableRepository;
//    private final UnitRepository unitRepository;
//
//
//    @Override
//    public void run(String... args) {
//
//        // role
//        if (roleRepository.findByName("USER").isEmpty()) {
//            Role user = new Role();
//            user.setName("USER");
//            roleRepository.save(user);
//        }
//
//        if (roleRepository.findByName("ADMIN").isEmpty()) {
//            Role user = new Role();
//            user.setName("ADMIN");
//            roleRepository.save(user);
//        }
//
//        //CategoryIngredient
//        CategoryIngredient category1 = new CategoryIngredient();
//        category1.setName("Rau củ");
//        categoryIngredientRepository.save(category1);
//        CategoryIngredient category2 = new CategoryIngredient();
//        category2.setName("Thịt");
//        categoryIngredientRepository.save(category2);
//        CategoryIngredient category3 = new CategoryIngredient();
//        category3.setName("Gia vị");
//        categoryIngredientRepository.save(category3);
//
//
//        //CategoryMenuItem
//        CategoryMenuItem item1 = new CategoryMenuItem();
//        item1.setName("Món chính");
//        categoryMenuItemRepository.save(item1);
//        CategoryMenuItem item2 = new CategoryMenuItem();
//        item2.setName("Món phụ");
//        categoryMenuItemRepository.save(item2);
//        CategoryMenuItem item3 = new CategoryMenuItem();
//        item3.setName("Nước uống");
//        categoryMenuItemRepository.save(item3);
//
//        //customer
//        Customer customer = new Customer();
//        customer.setFullName("Nguyễn Văn A");
//        customer.setPhone("0912345678");
//        customer.setEmail("nguyenvana@example.com");
//        customerRepository.save(customer);
//
//        // Unit
//        Unit unit1 = new Unit();
//        unit1.setName("Kg");
//        unitRepository.save(unit1);
//        Unit unit2 = new Unit();
//        unit2.setName("Lít");
//        unitRepository.save(unit2);
//        Unit unit3 = new Unit();
//        unit3.setName("Cái");
//        unitRepository.save(unit3);
//
//        //Account
//        Account user = new Account();
//        user.setUsername("user");
//        user.setPassword("user123");
//        user.setFullName("Nguyễn Văn A");
//        user.setPhone("0911222333");
//        user.setEmail("user@example.com");
//        user.setCreatedAt(LocalDateTime.now());
//        user.setAvatarUrl("https://example.com/avatar/user.png");
//        user.setRole(roleRepository.findByName("USER").get());
//        accountRepository.save(user);
//
//
//        // ingredient
//        Ingredient ingredient = new Ingredient();
//        ingredient.setName("Thịt bò");
//        ingredient.setDescription("Thịt bò tươi");
//        ingredient.setStatus(true);
//        ingredient.setMinQuantity(5);
//        ingredient.setQuantity(50);
//        ingredient.setPrice(BigDecimal.valueOf(150000));
//        ingredient.setUnit(unit1); // unit1 = "Kg"
//        ingredient.setCategory(category2); // category2 = "Thịt"
//        ingredient.setAccount(user);
//        ingredientRepository.save(ingredient);
//
//        // invoice
//        Invoice invoice = new Invoice();
//        invoice.setTotalAmount(BigDecimal.valueOf(40000));
//        invoice.setStatus(InvoiceStatus.DA_THANH_TOAN); // enum
//        invoice.setCreatedAt(LocalDateTime.now());
//        invoiceRepository.save(invoice);
//
//        // Location
//        Location location = new Location();
//        location.setName("Chi nhánh 1");
//        location.setAddress("123 Đường ABC, Quận 1, TP.HCM");
//        location.setOpenTime(LocalTime.of(8, 0));
//        location.setCloseTime(LocalTime.of(22, 0));
//        location.setLocationStatus(LocationStatus.HOAT_DONG); // Enum của bạn
//        locationRepository.save(location);
//
//        // MenuItem
//        MenuItem menuItem = new MenuItem();
//        menuItem.setName("Cơm tấm sườn");
//        menuItem.setDescription("Cơm tấm ăn kèm sườn nướng");
//        menuItem.setPrice(BigDecimal.valueOf(40000));
//        menuItem.setStatus(MenuItemStatus.DANG_BAN); // enum
//        menuItem.setCategory(item1);
//        menuItemRepository.save(menuItem);
//
//        // MenuItemIngredient
//        MenuItemIngredient miIngredient = new MenuItemIngredient();
//        miIngredient.setMenuItem(menuItem);
//        miIngredient.setIngredient(ingredient);
//        miIngredient.setQuantity(BigDecimal.valueOf(0.2));
//        menuItemIngredientRepository.save(miIngredient);
//
//        //image location
//        LocationImages locationImages = new LocationImages();
//        locationImages.setName("Ảnh 0");
//        locationImages.setUrl("https://example.com/anh0.jpg");
//        locationImages.setLocation(location);
//        locationImagesRepository.save(locationImages);
//
//        //image menuItem
//        MenuItemImages image2 = new MenuItemImages();
//        image2.setName("Ảnh 1");
//        image2.setUrl("https://example.com/anh1.jpg");
//        image2.setMenuItem(menuItem);
//        menuItemImagesRepository.save(image2);
//
//        // Table
//        Table table = new Table();
//        table.setName("Bàn 01");
//        table.setStatus(TableStatus.DANG_DON_DEP);
//        tableRepository.save(table);
//        Table table1 = new Table();
//        table1.setName("Bàn 02");
//        table1.setStatus(TableStatus.DANG_DON_DEP);
//        tableRepository.save(table1);
//        Table table2 = new Table();
//        table2.setName("Bàn 03");
//        table2.setStatus(TableStatus.DANG_DON_DEP);
//        tableRepository.save(table2);
//
//        // order
//        Order order = new Order();
//        order.setCustomer(customer);
//        order.setTable(table);
//        order.setStatusOrder(OrderStatus.DA_HOAN_THANH); // enum
//        order.setOrderType(OrderType.MANG_DI); // enum
//        order.setTotalPrice(BigDecimal.valueOf(40000));
//        order.setLocation(location);
//        order.setReservationDate(LocalDateTime.now());
//        order.setGuestCount(10);
//        order.setInvoice(invoice);
//        orderRepository.save(order);
//
//        // orderDetail
//        OrderDetail orderDetail = new OrderDetail();
//        orderDetail.setOrder(order);
//        orderDetail.setMenuItem(menuItem);
//        orderDetail.setQuantity(7);
//        orderDetail.setNote("Không hành");
//        orderDetail.setPriceAtOrder(BigDecimal.valueOf(40000));
//        orderDetailRepository.save(orderDetail);
//
//
//    }
//}
//
