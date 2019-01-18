package one.xingyi.core.codeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotations.Entity;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EntityDom {
    final PackageAndClassName entityName;
    final Optional<String> bookmark;
    final Optional<String> getUrl;
    final List<FieldDom> fields;

    static public EntityDom create(INames name, PackageAndClassName entityName, Entity entity, List<FieldDom> fields) {
        return new EntityDom(name.entityName(entityName, entity.entityName()),
                name.bookmark(entityName, entity.bookmark()),
                name.getUrl(entityName, entity.getUrl()),
                fields);
    }
}
