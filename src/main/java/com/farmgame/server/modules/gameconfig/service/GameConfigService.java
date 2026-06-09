package com.farmgame.server.modules.gameconfig.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.farmgame.server.common.api.ErrorCode;
import com.farmgame.server.common.exception.BizException;
import com.farmgame.server.modules.gameconfig.model.CropConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameConfigService {

    private final ObjectMapper objectMapper;
    private final Map<String, CropConfig> cropConfigMap = new HashMap<>();

    public GameConfigService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 启动时加载本地 JSON 配置。
     *
     * <p>MVP 阶段先使用 resources 下的 JSON 文件，后续可替换为 Excel 转 JSON 或后台配置。</p>
     */
    @PostConstruct
    public void load() throws IOException {
        List<CropConfig> crops = objectMapper.readValue(
                new ClassPathResource("game-config/crops.json").getInputStream(),
                new TypeReference<>() {
                }
        );
        cropConfigMap.clear();
        for (CropConfig crop : crops) {
            cropConfigMap.put(crop.getCropId(), crop);
        }
    }

    /**
     * 根据作物ID获取作物配置。
     *
     * @param cropId 作物ID
     * @return 作物配置
     */
    public CropConfig getCrop(String cropId) {
        CropConfig crop = cropConfigMap.get(cropId);
        if (crop == null) {
            throw new BizException(ErrorCode.BAD_REQUEST, "作物配置不存在：" + cropId);
        }
        return crop;
    }

    /**
     * 获取所有作物配置。
     *
     * @return 作物配置列表
     */
    public List<CropConfig> listCrops() {
        return List.copyOf(cropConfigMap.values());
    }
}
