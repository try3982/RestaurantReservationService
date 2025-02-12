package com.zerobase.restaurantreservation.partner.controller;

import com.zerobase.restaurantreservation.partner.dto.PartnerRegister;
import com.zerobase.restaurantreservation.partner.service.PartnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/signup")
public class PartnerController {
    private final PartnerService partnerService;

    // 파트너 회원가입 API
    @PostMapping("/partnerRegister")
    public ResponseEntity<PartnerRegister.Response> registerPartner(@RequestBody PartnerRegister.Request request) {
        return ResponseEntity.ok(partnerService.registerPartner(request));
    }
}
