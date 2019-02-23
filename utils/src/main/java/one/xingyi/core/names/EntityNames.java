package one.xingyi.core.names;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.codeDom.PackageAndClassName;

@ToString
@EqualsAndHashCode
public class EntityNames {
    public final PackageAndClassName originalDefn;
    public final PackageAndClassName serverInterface;
    public final PackageAndClassName serverEntity;
    public final PackageAndClassName serverCompanion;
    public final PackageAndClassName clientResource;
    public final PackageAndClassName clientResourceCompanion;
    public final PackageAndClassName serverController;
    public final String entityNameForLens;

    public EntityNames(PackageAndClassName originalDefn, PackageAndClassName serverInterface, PackageAndClassName serverEntity, PackageAndClassName serverCompanion, PackageAndClassName clientResource, PackageAndClassName clientResourceCompanion,PackageAndClassName serverController, String entityNameForLens) {
        this.originalDefn = originalDefn;
        this.serverInterface = serverInterface;
        this.serverEntity = serverEntity;
        this.serverCompanion = serverCompanion;
        this.clientResource = clientResource;
        this.clientResourceCompanion = clientResourceCompanion;
        this.serverController = serverController;
        this.entityNameForLens = entityNameForLens;
    }
}
