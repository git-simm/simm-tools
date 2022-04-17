package com.simm.bpm.service;

import com.simm.bpm.entity.BpmVersion;

import java.util.List;

/**
 * @author miscr
 */
public interface IBpmVersionQueryService {
    List<BpmVersion> getAll();
}
