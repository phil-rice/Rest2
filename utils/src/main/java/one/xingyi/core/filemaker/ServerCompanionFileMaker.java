package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.Entity;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.sdk.IXingYiEntity;
import one.xingyi.core.sdk.IXingYiServerCompanion;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.utils.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class ServerCompanionFileMaker implements IFileMaker<EntityDom> {

    List<String> createBookmark(EntityDom entityDom) {
        String bookmark = Optionals.fold(entityDom.bookmark, () -> "Optional.empty()", b -> "Optional.of(" + Strings.quote(b) + ")");
        return List.of("@Override public Optional<String> bookmark(){ return " + bookmark + "; }");
    }

    private List<String> createCompanion(EntityDom entityDom) {
        return List.of("public static " + entityDom.entityName.serverCompanion.asString() + " companion  =new " + entityDom.entityName.serverCompanion.className + "();");
    }
    private List<String> createJavascript(EntityDom entityDom) {

        List<String> result = new ArrayList<>();
        result.add("final public String javascript = " + Strings.quote(""));
        result.addAll(Formating.indent(entityDom.fields.map(fd -> "+ " + Strings.quote("function " + fd.lensName + "(){ return lens('" + fd.lensPath + "');};\\n"))));
        result.add(";");
        return result;
    }

    @Override public FileDefn apply(EntityDom entityDom) {
        String implementsString = "IXingYiServerCompanion<" + entityDom.entityName.originalDefn.asString() + "," + entityDom.entityName.serverEntity.asString() + ">";
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), "class", entityDom.entityName.serverCompanion,
                        " implements " + implementsString, List.of(), IXingYiServerCompanion.class, XingYiGenerated.class, Optional.class),
//                Formating.indent(allFieldsAccessors(entityDom.entityName.serverInterface.className, entityDom.fields)),
                Formating.indent(createBookmark(entityDom)),
                Formating.indent(createCompanion(entityDom)),
                Formating.indent(createJavascript(entityDom)),
                List.of("}")
        ), "\n");
        return new FileDefn(entityDom.entityName.serverCompanion, result);
    }
}
