package org.example.bai5ss9;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.example.bai5ss9.config.AppConfig;

@SpringJUnitConfig
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration("src/main/webapp")
class HomeControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldRenderEnglishByDefault() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello")))
                .andExpect(content().string(containsString("Cart")))
                .andExpect(content().string(containsString("Promotions")));
    }

    @Test
    void shouldSwitchToJapaneseAndStoreCookie() throws Exception {
        mockMvc.perform(get("/").param("lang", "ja"))
                .andExpect(status().isOk())
                .andExpect(cookie().value("rikkeiMallLocale", "ja"))
                .andExpect(content().string(containsString("\u3053\u3093\u306b\u3061\u306f")))
                .andExpect(content().string(containsString("\u30ab\u30fc\u30c8")))
                .andExpect(content().string(containsString("\u30d7\u30ed\u30e2\u30fc\u30b7\u30e7\u30f3")));
    }

    @Test
    void shouldSwitchToKoreanAndStoreCookie() throws Exception {
        mockMvc.perform(get("/").param("lang", "ko"))
                .andExpect(status().isOk())
                .andExpect(cookie().value("rikkeiMallLocale", "ko"))
                .andExpect(content().string(containsString("\uc548\ub155\ud558\uc138\uc694")))
                .andExpect(content().string(containsString("\uc7a5\ubc14\uad6c\ub2c8")))
                .andExpect(content().string(containsString("\ud504\ub85c\ubaa8\uc158")));
    }

    @Test
    void shouldFallbackToEnglishWhenBundleDoesNotExist() throws Exception {
        mockMvc.perform(get("/").param("lang", "ar"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello")))
                .andExpect(content().string(containsString("Cart")))
                .andExpect(content().string(containsString("Promotions")));
    }
}
