package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiProductInfoController {

    @Autowired
    private AiProductInfoService aiProductInfoService;

    @GetMapping("/api/product/{productName}/ai-info")
    public AiProductInfoService.AiProductInfoDTO getAiProductInfo(@PathVariable("productName") String productName) {
        return aiProductInfoService.getAiProductInfo(productName);
    }
}
