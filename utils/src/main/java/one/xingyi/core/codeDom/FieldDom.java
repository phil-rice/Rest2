package one.xingyi.core.codeDom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotations.Field;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.typeDom.TypeDom;
import one.xingyi.core.utils.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class FieldDom {
    public final TypeDom typeDom;
    public final String name;
    public final boolean readOnly;
    public final String lensName;
    public final String lensPath;
    public final String javascript;
    public final boolean templated;
//    public static FieldDom apply(IServerNames names, EntityNames entityNames, TypeDom typeDom, String fieldName, Field field) {
//        String lensName = names.entityLensName(entityNames, fieldName, field.lensName());
//        String lensPath = names.entityLensPath(entityNames, fieldName, field.lensPath());
//        String javascript = Strings.from(field.javascript(), "function lens_" + lensName + "(){ return lens('" + lensPath + "');};");
//        return new FieldDom(typeDom, fieldName, field.readOnly(), lensName, lensPath, javascript);
//
//    }
}
