package com.simm.web.controller;

import com.simm.bpm.entity.BpmVersion;
import com.simm.bpm.service.IBpmVersionQueryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试
 *
 * @author miscr
 */
@RestController
@RequestMapping("bpm")
@AllArgsConstructor
public class BpmController {
    final IBpmVersionQueryService bpmVersionQueryService;

    @GetMapping("list")
    public List<BpmVersion> getAllVersion() {
        return bpmVersionQueryService.getAll();
    }
}
