package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.codeDom.ViewDomAndItsEntityDom;
import one.xingyi.core.sdk.IXingYiClientEntity;
import one.xingyi.core.sdk.IXingYiClientImpl;
import one.xingyi.core.sdk.IXingYiEntity;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;

import java.util.ArrayList;
import java.util.List;
public class ClientEntityFileMaker implements IFileMaker<ViewDomAndItsEntityDom> {


    @Override public FileDefn apply(ViewDomAndItsEntityDom viewAndEntityDom) {
        ViewDom viewDom = viewAndEntityDom.viewDom;
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(),"interface", viewDom.viewNames.clientEntity,
                        " extends IXingYiClientEntity", List.of(),
                        IXingYiClientEntity.class, XingYiGenerated.class),
                 List.of("}")
        ), "\n");
        return new FileDefn(viewDom.viewNames.clientEntity, result);
    }
}