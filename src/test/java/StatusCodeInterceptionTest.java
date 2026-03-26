import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;

public class StatusCodeInterceptionTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeEach
    public void setup(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    public void tearDown() {
        if (page != null) page.close();
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @Test
    public void testMockedStatusCode(){
        // 1. Сначала настраиваем маршрут перехвата
        context.route("**/status_code/404", route -> {
            route.fulfill(new Route.FulfillOptions()
                    .setStatus(200)
                    .setHeaders(Collections.singletonMap("Content-Type", "text/html"))
                    .setBody("<h3>Mocked Success</h3>")
            );
        });

        page.navigate("https://the-internet.herokuapp.com/status_codes");
    }
}
