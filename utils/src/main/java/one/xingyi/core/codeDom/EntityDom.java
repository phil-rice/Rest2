package one.xingyi.core.codeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotations.Entity;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.validation.Result;

import java.util.Optional;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EntityDom {
    public final EntityNames entityName;
    public final Optional<String> bookmark;
    public final Optional<String> getUrl;
    public final FieldListDom fields;

    static public Result<String, EntityDom> create(IServerNames name, String interfaceDefnName, Entity entity, FieldListDom fields) {
        return name.entityName(interfaceDefnName, entity.entityName()).
                map(entityNames -> new EntityDom(entityNames, name.bookmark(entityNames, entity.bookmark()), name.getUrl(entityNames, entity.getUrl()), fields));
    }
}
