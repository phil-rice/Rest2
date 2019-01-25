package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.EntityDom;
import one.xingyi.core.endpoints.BookmarkAndUrlPattern;
import one.xingyi.core.endpoints.HasBookmarkAndUrl;
import one.xingyi.core.sdk.IXingYiServerCompanion;
import one.xingyi.core.sdk.IXingYiServesEntityCompanion;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class ServerCompanionFileMaker implements IFileMaker<EntityDom> {

    List<String> createBookmarkAndUrl(EntityDom entityDom) {
        return Optionals.fold(entityDom.bookmark, () -> List.of(), b -> List.of("@Override public BookmarkAndUrlPattern bookmarkAndUrl(){return new BookmarkAndUrlPattern(" + Strings.quote(b.bookmark) + "," + Strings.quote(b.urlPattern) + ");}"));
    }



    private List<String> createCompanion(EntityDom entityDom) {
        return List.of("public static " + entityDom.entityNames.serverCompanion.asString() + " companion  =new " + entityDom.entityNames.serverCompanion.className + "();");
    }
    private List<String> createEntityEndpoint(EntityDom entityDom) {
        return List.of("public static " + entityDom.entityNames.serverCompanion.asString() + " companion  =new " + entityDom.entityNames.serverCompanion.className + "();");
    }
    private List<String> createJavascript(EntityDom entityDom) {

        List<String> result = new ArrayList<>();
        result.add("public String javascript(){return javascript;}");
        result.add("final public String javascript = " + Strings.quote(""));
        result.addAll(Formating.indent(entityDom.fields.map(fd -> "+ " + Strings.quote("function " + fd.lensName + "(){ return lens('" + fd.lensPath + "');};\\n"))));
        result.add(";");
        return result;
    }

    @Override public Result<String, FileDefn> apply(EntityDom entityDom) {
        String implementsString = (entityDom.bookmark.isEmpty() ?"IXingYiServerCompanion": "IXingYiServesEntityCompanion") + "<" + entityDom.entityNames.originalDefn.asString() + "," + entityDom.entityNames.serverEntity.asString() + ">";
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(),entityDom.entityNames.originalDefn, "class", entityDom.entityNames.serverCompanion,
                        " implements " + implementsString, List.of(), IXingYiServerCompanion.class, IXingYiServesEntityCompanion.class, XingYiGenerated.class, Optional.class, BookmarkAndUrlPattern.class, HasBookmarkAndUrl.class),
//                Formating.indent(allFieldsAccessors(entityDom.entityNames.serverInterface.className, entityDom.fields)),
                Formating.indent(createBookmarkAndUrl(entityDom)),
                Formating.indent(createCompanion(entityDom)),
                Formating.indent(createJavascript(entityDom)),
                List.of("}")
        ), "\n");
        return Result.succeed( new FileDefn(entityDom.entityNames.serverCompanion, result));
    }
}
