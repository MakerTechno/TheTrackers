package nowebsite.makertechno.the_trackers.core.config;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EntityType;
import nowebsite.makertechno.the_trackers.client.gui.cursors.*;
import nowebsite.makertechno.the_trackers.client.gui.components.Icon;
import nowebsite.makertechno.the_trackers.client.gui.components.BasicComponentFactory;
import nowebsite.makertechno.the_trackers.client.gui.components.BaseComponent;
import nowebsite.makertechno.the_trackers.client.gui.provider.TextureCache;
import nowebsite.makertechno.the_trackers.core.event.TModClient;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class ConfigProcessor {

    // 格式: entityType|pointer|entityIcon(|optionalPatterns)
    public static boolean isValidEntityBindCRCursor(Object o) {
        CRCursorParseData data = parseCRCursorData(o);
        return validateCRCursorData(data);
    }

    /**
     * 通过配置表处理CenterRelative指针数据
     */
    @NotNull
    public static Set<Pair<EntityType<?>, Supplier<? extends TRenderCursor>>> collectCREntityBindCursor(@NotNull List<? extends String> list) {
        return list.stream()
            .map(ConfigProcessor::parseCRCursorData)  // 处理为中间数据件
            .filter(ConfigProcessor::validateCRCursorData) // 验证配置完整性
            .map(ConfigProcessor::buildCRCursorPair) // 构造指针
            .collect(Collectors.toSet());
    }

    // 格式: entityType|type:icon(|optionalPattern)
    public static boolean isValidCREntityBindDTCursor(Object o) {
        DTCursorParseData data = parseDTCursorData(o);
        return validateDTCursorData(data);
    }

    /**
     * 通过配置表处理DirectProj指针数据
     */
    @NotNull
    public static Set<Pair<EntityType<?>, Supplier<? extends TRenderCursor>>> collectDTEntityBindCursor(@NotNull List<? extends String> list) {
        return list.stream()
            .map(ConfigProcessor::parseDTCursorData)
            .filter(ConfigProcessor::validateDTCursorData)
            .map(ConfigProcessor::buildDTCursorPair)
            .collect(Collectors.toSet());
    }
    /**
     * 处理CenterRelative配置
     */
    private static CRCursorParseData parseCRCursorData(Object o) {
        CRCursorParseData data = new CRCursorParseData();

        // 步骤1: 类型检查
        if (!(o instanceof String config)) return data.invalid();

        // 步骤2: 分割验证
        data.parts = config.split("\\|");
        if (data.parts.length < 3 || data.parts.length > 4) return data.invalid();

        // 步骤3: 解析实体类型
        Optional<EntityType<?>> entityType = EntityType.byString(data.parts[0]);
        if (entityType.isEmpty()) return data.invalid();
        data.entityType = entityType.get();

        // 步骤4: 读取图标键名
        data.pointerKey = data.parts[1];
        data.iconKey = data.parts[2];

        // 步骤5: 处理可选模式
        if (data.parts.length == 4) {
            String[] patterns = data.parts[3].split("&");
            if (patterns.length != 2) return data.invalid();
            data.pattern1 = patterns[0];
            data.pattern2 = patterns[1];
            data.hasPatterns = true;
        }

        return data.valid();
    }

    private static boolean validateCRCursorData(CRCursorParseData data) {
        if (!data.valid) return false;

        // 验证图标有效性（仅在客户端加载时）
        if (TModClient.isLoaded) {
            if (!data.pointerKey.equals("none") && TextureCache.getIcon(data.pointerKey).equals(Icon.NONE))
                return false;
            if (!data.iconKey.equals("none") && TextureCache.getIcon(data.iconKey).equals(Icon.NONE))
                return false;
        }

        // 验证模式有效性（如果有）
        if (data.hasPatterns) {
            if (!BasicComponentFactory.hasElementPattern(data.pattern1)) return false;
            return BasicComponentFactory.hasElementPattern(data.pattern2);
        }

        return true;
    }

    private static Pair<EntityType<?>, Supplier<? extends TRenderCursor>> buildCRCursorPair(CRCursorParseData data) {
        Icon pointerIcon = TextureCache.getIcon(data.pointerKey);
        Icon icon = TextureCache.getIcon(data.iconKey);

        Supplier<? extends TRenderCursor> supplier;

        if (data.hasPatterns) {
            supplier = () -> new TRelativeCursor(
                BasicComponentFactory.getElementComponent(pointerIcon, data.pattern1).get(),
                BasicComponentFactory.getElementComponent(icon, data.pattern2).get()
            );
        } else {
            supplier = () -> new TRelativeCursor(
                BasicComponentFactory.getDefault(pointerIcon).get(),
                BasicComponentFactory.getDefault(icon).get()
            );
        }

        return new Pair<>(data.entityType, supplier);
    }

    private static DTCursorParseData parseDTCursorData(Object o) {
        DTCursorParseData data = new DTCursorParseData();

        // 步骤1: 类型检查
        if (!(o instanceof String config)) return data.invalid();

        // 步骤2: 分割验证
        data.parts = config.split("\\|");
        if (data.parts.length < 2 || data.parts.length > 3) return data.invalid();

        // 步骤3: 解析实体类型
        Optional<EntityType<?>> typeOpt = EntityType.byString(data.parts[0]);
        if (typeOpt.isEmpty()) return data.invalid();
        data.entityType = typeOpt.get();

        // 步骤4: 解析样式和图标
        String[] styleParts = data.parts[1].split(":");
        if (styleParts.length != 2) return data.invalid();
        data.style = styleParts[0];
        data.iconKey = styleParts[1];

        // 步骤5: 处理可选模式
        if (data.parts.length == 3) {
            data.optionalPattern = data.parts[2];
            data.hasOptionalPattern = true;
        }

        return data.valid();
    }

    private static boolean validateDTCursorData(DTCursorParseData data) {
        if (!data.valid) return false;

        // 验证样式类型
        if (!data.style.equals("normal") && !data.style.equals("3body")) return false;

        // 验证图标有效性
        if (TModClient.isLoaded && !data.iconKey.equals("none")) {
            if (TextureCache.getIcon(data.iconKey).equals(Icon.NONE)) return false;
        }

        // 验证可选模式（如果有）
        if (data.hasOptionalPattern) {
            return BasicComponentFactory.hasElementPattern(data.optionalPattern);
        }

        return true;
    }

    private static Pair<EntityType<?>, Supplier<? extends TRenderCursor>> buildDTCursorPair(DTCursorParseData data) {
        Icon icon = TextureCache.getIcon(data.iconKey);

        Supplier<BaseComponent> componentSupplier;
        if (data.hasOptionalPattern) {
            componentSupplier = BasicComponentFactory.getElementComponent(icon, data.optionalPattern);
        } else {
            componentSupplier = BasicComponentFactory.getDefault(icon);
        }

        Supplier<? extends TRenderCursor> supplier;
        if (data.style.equals("3body")) {
            supplier = () -> new TDir3BodyCursor(
                componentSupplier.get(),
                componentSupplier.get(),
                componentSupplier.get()
            );
        } else { // "normal"
            supplier = () -> new TDirectProjCursor(componentSupplier.get());
        }

        return new Pair<>(data.entityType, supplier);
    }

    // ==================== 内部数据结构 ====================

    private static class CRCursorParseData {
        private boolean valid = false;
        private String[] parts;
        private EntityType<?> entityType;
        private String pointerKey;
        private String iconKey;
        private boolean hasPatterns = false;
        private String pattern1;
        private String pattern2;

        private CRCursorParseData valid() { this.valid = true; return this; }
        private CRCursorParseData invalid() { this.valid = false; return this; }
    }

    private static class DTCursorParseData {
        private boolean valid = false;
        private String[] parts;
        private EntityType<?> entityType;
        private String style;
        private String iconKey;
        private boolean hasOptionalPattern = false;
        private String optionalPattern;

        private DTCursorParseData valid() { this.valid = true; return this; }
        private DTCursorParseData invalid() { this.valid = false; return this; }
    }
}