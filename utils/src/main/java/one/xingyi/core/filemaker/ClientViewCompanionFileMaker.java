package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.codeDom.ViewDomAndItsEntityDom;
import one.xingyi.core.sdk.IXingYiClientViewCompanion;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;

import java.util.ArrayList;
import java.util.List;
public class ClientViewCompanionFileMaker implements IFileMaker<ViewDomAndItsEntityDom> {

    @Override public FileDefn apply(ViewDomAndItsEntityDom viewDomAndItsEntityDom) {
        ViewDom viewDom = viewDomAndItsEntityDom.viewDom;
        List<String> manualImports = Lists.unique(viewDom.fields.map(fd -> fd.typeDom.fullTypeName()));
//        String afterClassString = "<Entity extends IXingYiEntity, IOps extends IXingYiView<Entity>, Impl extends IXingYiClientImpl<Entity, IOps>> extends IXingYiClientMaker<Entity, IOps>"
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(),"interface", viewDom.viewNames.clientCompanion,
                        " extends IXingYiClientViewCompanion<" +
                                viewDom.viewNames.clientEntity.asString() + "," +
                                viewDom.viewNames.clientView.asString() + "," +
                                viewDom.viewNames.clientViewImpl.asString() +
                                ">", manualImports, IXingYiView.class, XingYiGenerated.class, IXingYiClientViewCompanion.class),
                List.of("}")
        ), "\n");
        return new FileDefn(viewDom.viewNames.clientCompanion, result);
    }
}
