# Bai 5 - "Nhap gia tuy tuc" (I18N)

## 1. Kien truc va logic

### Cong cu Spring dung de "danh chan" URL doi ngon ngu

- `LocaleChangeInterceptor` la interceptor cua Spring MVC. No chan request truoc khi controller xu ly, doc query parameter `lang` va yeu cau `LocaleResolver` doi locale hien tai.
- Trong bai nay, `lang` duoc cau hinh o [AppConfig.java](./src/main/java/org/example/bai5ss9/config/AppConfig.java).

### Lua chon bo nho ngon ngu

- Chon `CookieLocaleResolver`.
- Ly do: yeu cau cua sep la "hom sau vao lai web van nho". `SessionLocaleResolver` chi song trong session hien tai; dong trinh duyet hoac het session la mat. `CookieLocaleResolver` ghi gia tri locale xuong cookie, vi vay khach quay lai vao ngay hom sau van giu duoc ngon ngu da chon.

### Data flow

```text
Nguoi dung bam "Japanese" hoac "Korean"
        |
        v
GET /?lang=ja  (hoac /?lang=ko)
        |
        v
DispatcherServlet
        |
        v
LocaleChangeInterceptor doc tham so lang
        |
        v
CookieLocaleResolver luu locale vao cookie rikkeiMallLocale (30 ngay)
        |
        v
HomeController tra ve view "index"
        |
        v
Thymeleaf render #{...}
        |
        v
MessageSource nap messages_ja.properties / messages_ko.properties
        |
        v
Tra HTML da duoc dich cho trinh duyet
```

Neu request la `/?lang=ar`:

```text
LocaleChangeInterceptor van nhan gia tri ar
        |
        v
MessageSource tim messages_ar.properties -> khong co
        |
        v
Fallback ve messages.properties (Tieng Anh mac dinh)
        |
        v
Trang van render binh thuong, khong loi trang trang
```

## 2. Thanh phan source code chinh

- DispatcherServlet initializer: [WebInit.java](/C:/Users/Admin/IdeaProjects/session9/bai5-ss9/src/main/java/org/example/bai5ss9/config/WebInit.java)
- Cau hinh MVC/i18n: [AppConfig.java](/C:/Users/Admin/IdeaProjects/session9/bai5-ss9/src/main/java/org/example/bai5ss9/config/AppConfig.java)
- Controller: [HomeController.java](/C:/Users/Admin/IdeaProjects/session9/bai5-ss9/src/main/java/org/example/bai5ss9/controller/HomeController.java)
- Template duy nhat: [index.html](/C:/Users/Admin/IdeaProjects/session9/bai5-ss9/src/main/webapp/WEB-INF/views/index.html)
- Resource bundle:
  - [messages.properties](/C:/Users/Admin/IdeaProjects/session9/bai5-ss9/src/main/resources/messages.properties) - fallback/Tieng Anh
  - [messages_ja.properties](/C:/Users/Admin/IdeaProjects/session9/bai5-ss9/src/main/resources/messages_ja.properties)
  - [messages_ko.properties](/C:/Users/Admin/IdeaProjects/session9/bai5-ss9/src/main/resources/messages_ko.properties)

## 3. Kich ban test rui ro `?lang=ar`

### Mo ta thao tac

1. Mo trinh duyet tai `/`.
2. Sua URL thanh `http://localhost:8080/?lang=ar`.
3. Ket qua mong doi:
   - Trang khong sap, khong loi 500, khong trang trang.
   - Noi dung van hien thi bang Tieng Anh: `Hello`, `Cart`, `Promotions`.
   - Ly do: `messages_ar.properties` khong ton tai, Spring fallback ve `messages.properties`.

### Test tu dong da them

- [HomeControllerTest.java](/C:/Users/Admin/IdeaProjects/session9/bai5-ss9/src/test/java/org/example/bai5ss9/HomeControllerTest.java)
- Case `shouldFallbackToEnglishWhenBundleDoesNotExist()` kiem tra dung kich ban `?lang=ar`.

## 4. Cach chay

Can JDK 17+ va Tomcat 10.1+ (hoac servlet container tuong thich Jakarta Servlet 6).

```powershell
./gradlew.bat war
```

Sau do deploy file WAR trong `build/libs/` len Tomcat va mo:

- `http://localhost:8080/<ten-war>/`
- `http://localhost:8080/<ten-war>/?lang=ja`
- `http://localhost:8080/<ten-war>/?lang=ko`
- `http://localhost:8080/<ten-war>/?lang=ar`
