package com.simm.web.service;

import com.simm.bpm.entity.CommitInfo;

/**
 * gitlab服务
 * @author miscr
 */
public interface IGitlabService {
    /**
     * 获取版本号
     * @return
     */
    String getVersion();

    /**
     * 获取文件内容
     * @param filePath 文件路径
     * @return 内容
     */
    String getFileContent(String filePath);

    /**
     * 提交文件
     * @param filePath 文件路径
     * @param commitInfo 提交内容
     */
    String commitFile(String filePath, CommitInfo commitInfo);

    /**
     * 更新版本号
     * @return 更新结果
     */
    String updateVersion();
}
