
package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import java.nio.file.Paths;
import java.util.List;

public class DriverPage
{
    public static void main(String[] args)
    {

        Playwright playwright = Playwright.create();

        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setExecutablePath(Paths.get(
                                "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"))
                        .setHeadless(false)
                        .setArgs(List.of("--start-maximized"))
        );

        BrowserContext context = browser.newContext(
                new Browser.NewContextOptions().setViewportSize(null)
        );

        Page page = context.newPage();

        page.setDefaultTimeout(10000);

        // Handle and print the JavaScript popup message
        page.onDialog(dialog -> {
            System.out.println("Popup message: " + dialog.message());
            dialog.accept();
        });

        page.navigate("https://www.automationtesting.co.uk/loginPortal.html");

        page.getByPlaceholder("Username").fill("test");
        page.getByPlaceholder("Password").fill("test123");

        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Login")
        ).click();

        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Accordion")).click();
        page.locator("//*[@id=\"main\"]/div/div/div/div[1]").click();

        page.waitForTimeout(5000);


        page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Buttons")).click();

        page.locator("button")
                .filter(new Locator.FilterOptions().setHasText("Button Two"))
                .evaluate("element => element.click()");

        Locator button  = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Button Three"));
        button.hover();
        button.click();

        Locator bf = page.getByText("Button Four");
        System.out.println(bf.isDisabled());

        // Close resources when finished
        browser.close();
        playwright.close();
    }
}
