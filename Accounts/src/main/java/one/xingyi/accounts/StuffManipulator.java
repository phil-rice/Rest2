package one.xingyi.accounts;
import one.xingyi.accounts.server.controller.IAccountsController;
import one.xingyi.accounts.server.domain.Accounts;
import one.xingyi.core.utils.IdAndValue;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
public class StuffManipulator implements IAccountsController {
    @Override public String stateFn(Accounts entity) {
        return "";
    }
    @Override public CompletableFuture<Accounts> put(IdAndValue<Accounts> idAndAccounts) {
        return null;
    }
    @Override public CompletableFuture<Optional<Accounts>> getOptional(String id) {
        return CompletableFuture.completedFuture(Optional.of(new Accounts("someStuff")));
    }
    @Override public CompletableFuture<Boolean> delete(String id) {
        return null;
    }
    @Override public CompletableFuture<Accounts> create(String id) {
        return null;
    }
    @Override public CompletableFuture<IdAndValue<Accounts>> create() {
        return null;
    }
}
