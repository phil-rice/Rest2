package one.xingyi.trafficlights;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.sdk.IXingYiEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

interface XingYiState<Entity extends IXingYiEntity> {}
