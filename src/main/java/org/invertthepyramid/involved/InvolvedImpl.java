package org.invertthepyramid.involved;

import com.twitter.finagle.Service;
import com.twitter.util.Future;
import org.apache.commons.collections.CollectionUtils;
import org.invertthepyramid.involved.domain.*;
import org.invertthepyramid.involved.misc.*;
import org.invertthepyramid.involved.search.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import scala.runtime.AbstractFunction1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.invertthepyramid.involved.Wrap.*;

public class InvolvedImpl implements Involved {
    @Value("${Alert.alert.dtapstage}")
    private String AlertDtapStage;
    @Autowired
    Service<RequestChain, ResponseChain> mdmService;

    @Autowired
    AlertReporter report;

    private static LoggerAdapter log = null;//would normally be Logger.get... etc

    AbstractService<UpdateAddressRequest, PartyAddress> updateAddress() {
        return new AbstractService<>(IErrorStrategy.checkConnection(log, report), mdmService, UpdateAddressRequest.makeRequestChain, PartyAddress.fromResponseChain);
    }

    public InvolvedImpl() {
    }

    InvolvedImpl(LoggerAdapter log, AlertReporter reporter, Service<RequestChain, ResponseChain> mdmService) {
        this.log = log;
        this.report = reporter;
        this.mdmService = mdmService;
    }

    @Override
    public PartyAddress updateAddress(PartyAddress partyAddress, String lastUpdateUser, List<String> userRoles) {
        return updateAddress().apply(new UpdateAddressRequest(partyAddress, lastUpdateUser, userRoles));
    }



    AbstractService<GetAddressRequest, PartyAddress> getAddress() {
        return new AbstractService<GetAddressRequest, PartyAddress>(IErrorStrategy.checkConnection(log, report), mdmService,
                GetAddressRequest.makeRequestChain,
                (responseChain) -> {
                    final Response response = responseChain.getOptionalResponse(0, "893");
                    if (response.getStatus() == ResponseStatus.SUCCESS) {
                        return PartyAddress.fromResponse(response.getObject(0), true);
                    } else {
                        return null;
                    }
                });
    }

    @Override
    public PartyAddress getAddress(String partyAddressIdPK, List<String> userRoles) {
        return getAddress().apply(new GetAddressRequest(partyAddressIdPK, userRoles));
    }


    @Override
    public ContactMethod getContactMethod(String partyContactMethodIdPK, boolean keepObjectAlive, List<String> userRoles) {
        return wrap(IErrorStrategy.checkConnection(log, report), map(
                mdmService.apply(ContactMethod.getPartyContactMethodByIdPK(partyContactMethodIdPK).toChain("getPartyContactMethodByIdPK").withRole(userRoles)),
                (responseChain -> {
                    final Response response = responseChain.getOptionalResponse(0, "893");
                    if (response.getStatus() == ResponseStatus.SUCCESS) {
                        return ContactMethod.fromResponse(response.getObject(0), keepObjectAlive);
                    } else {
                        return null;
                    }
                }))).get();
    }


    @Override
    public ContactMethod updateContactMethod(ContactMethod contactMehod, String
            lastUpdateUser, List<String> userRoles) {
        try {

            Future<ContactMethod> responseFuture = mdmService.apply(contactMehod.toCommand().toChain("updatePartyContactMethod").withRequesterName(lastUpdateUser).withRole(userRoles))
                    .map(new AbstractFunction1<ResponseChain, ContactMethod>() {
                        @Override
                        public ContactMethod apply(ResponseChain responseChain) {
                            return ContactMethod.fromResponse(responseChain.getSafeResponse(0).getObject(0));
                        }
                    });

            return Function.destroyMyPerformance(responseFuture);

        } catch (MDMServiceException mdmec) {
            throw handleMdmException(mdmec);
        }
    }

    @Override
    public Contract searchContract(String contractType, String contractId, List<String> userRoles) {
        try {
            Future<Contract> responseFuture = null;
            final SearchContract searchContract = Contract.searchContract(contractType, contractId);
            searchContract.setInquiryLevel(ApplicationConstant.INQUIRY_LEVEL_1);
            responseFuture = mdmService.apply(searchContract.toCommand().toChain("ETC").withRole(userRoles))
                    .map(new AbstractFunction1<ResponseChain, Contract>() {
                        @Override
                        public Contract apply(ResponseChain responseChain) {
                            final Response response = responseChain.getOptionalResponse(0, "794");
                            if (response.getStatus() == ResponseStatus.NOT_FOUND) {
                                return null;
                            } else {
                                return Contract.fromResponse(responseChain.getSafeResponse(0).getObject(0));
                            }
                        }
                    });
            return Function.destroyMyPerformance(responseFuture);
        } catch (MDMServiceException mdmec) {
            throw new InvolvedException(ApplicationConstant.ERROR_CODE_0, ApplicationConstant.ERROR_CANNOT_EXECUTE_REQUEST, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Person getPartyDetails(String partyId, List<String> userRoles) {
        try {
            Future<Person> responseFuture = null;
            Request getAllPartyAdminSysKeys = PartyAdminSysKey.getAllPartyAdminSysKeys(partyId);
            final GetParty getParty = Party.getParty(partyId, ApplicationConstant.PERSON_PARTY_TYPE, ApplicationConstant.INQUIRY_LEVEL_1);
            final RequestChain getPartyRequestChain = getParty.toChain("getParty").withRequest(getAllPartyAdminSysKeys).withRole(userRoles);

            responseFuture = mdmService.apply(getPartyRequestChain)
                    .map(new AbstractFunction1<ResponseChain, Person>() {
                        @Override
                        public Person apply(ResponseChain responseChain) {
                            final Response response = responseChain.getOptionalResponse(0, "893");
                            if (response.getStatus() == ResponseStatus.NOT_FOUND) {
                                return null;
                            } else {
                                Person person = Person.fromResponse(response.getObject(0), false);
                                if (CollectionUtils.isNotEmpty(responseChain.getSafeResponse(1).getObjects())) {
                                    for (CommonBObjType commonBObjType : responseChain.getSafeResponse(1).getObjects()) {
                                        person.addAdminSysKey(PartyAdminSysKey.fromResponse(commonBObjType));
                                    }
                                } else {
                                    log.warn("AdminSysKeys not received for partyId : {}", partyId);
                                }
                                return person;
                            }
                        }
                    });
            return Function.destroyMyPerformance(responseFuture);

        } catch (MDMServiceException mdmec) {
            log.error("Exception Message : ", mdmec);
            report.reportAlert(mdmec);
            throw new InvolvedException(ApplicationConstant.ERROR_CODE_0, ApplicationConstant.ERROR_CANNOT_EXECUTE_REQUEST, Status.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public PartySearchResponse searchParty(SearchIndividualRequest individualSearch, SearchOrganisationRequest
            organisationSearch, List<String> userRoles) {
        try {
            RequestChain requestChain = new RequestChain();
            if (individualSearch != null) {
                final SearchPerson searchPerson = Party.searchPerson(ApplicationConstant.INQUIRY_1, ApplicationConstant.INQUIRY_1, ApplicationConstant.INQUIRY_1);
                if (individualSearch.getLastName() != null && !individualSearch.getLastName().equals("")) {
                    searchPerson.setLastName(individualSearch.getLastName());
                }
                if (individualSearch.getElectronicAddress() != null) {
                    searchPerson.setContactMethodType(individualSearch.getElectronicAddress().getType());
                    searchPerson.setContactmethodValue(individualSearch.getElectronicAddress().getValue());
                }
                if (individualSearch.getDateOfBirth() != null) {
                    searchPerson.setDateOfBirth(individualSearch.getDateOfBirth());
                }
                requestChain = searchPerson.toCommand().toChain("searchParty").withRole(userRoles).withPageStartIndex("0").withPageEndIndex("10");
            } else if (organisationSearch != null) {
                final SearchOrganization searchParty = Party.searchOrganization(ApplicationConstant.INQUIRY_1, ApplicationConstant.INQUIRY_1, ApplicationConstant.INQUIRY_1);
                if (organisationSearch.getName() != null) {
                    searchParty.setName(organisationSearch.getName());
                }
                if (organisationSearch.getExternalIdentifier() != null) {
                    searchParty.setIdentificationType(organisationSearch.getExternalIdentifier().getType());
                    searchParty.setIdentificationNumber(organisationSearch.getExternalIdentifier().getId());
                }
                requestChain = searchParty.toCommand().toChain("searchParty").withRole(userRoles).withPageStartIndex("0").withPageEndIndex("10");
            }

            Future<PartySearchResponse> responseFuture = mdmService.apply(requestChain)
                    .map(new AbstractFunction1<ResponseChain, PartySearchResponse>() {
                        @Override
                        public PartySearchResponse apply(ResponseChain responses) {
                            PartySearchResponse partySearchResult = new PartySearchResponse();
                            List<PersonSearch> personSearchResult = new ArrayList<PersonSearch>();
                            List<OrganizationSearch> organizationSearchResult = new ArrayList<OrganizationSearch>();
                            Response mdmResponse = responses.getOptionalResponse(0, "794");

                            if (mdmResponse.getStatus() == ResponseStatus.NOT_FOUND) {
                                return null;
                            } else {
                                personSearchResult = new ArrayList<PersonSearch>();
                                for (CommonBObjType commonBObjType : mdmResponse.getObjects()) {
                                    if (commonBObjType instanceof TCRMPersonSearchResultBObjType) {
                                        personSearchResult.add(PersonSearch.fromResponse(commonBObjType, true));
                                    } else if (commonBObjType instanceof TCRMOrganizationSearchResultBObjType) {
                                        organizationSearchResult.add(OrganizationSearch.fromResponse(commonBObjType, true));
                                    }
                                }
                                partySearchResult.setOrganizations(organizationSearchResult);
                                partySearchResult.setPersons(personSearchResult);

                                return partySearchResult;
                            }
                        }
                    });
            return Function.destroyMyPerformance(responseFuture);

        } catch (MDMServiceException mdmec) {

            log.error("Exception Message : ", mdmec);
            throw new InvolvedException(ApplicationConstant.ERROR_CODE_0, ApplicationConstant.ERROR_CANNOT_EXECUTE_REQUEST, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public DeleteINGPartyWithAncestors deleteParty(String adminSysKeyType, String
            adminSysKey, List<String> userRoles) {
        try {
            Future<DeleteINGPartyWithAncestors> responseFuture = null;
            final DeleteINGPartyWithAncestors deleteParty = Party.deleteINGPartyWithAncestors(adminSysKeyType, adminSysKey);
            responseFuture = mdmService.apply(deleteParty.toCommand().toChain("deleteINGPartyWithAncestors").withRole(userRoles))
                    .map(new AbstractFunction1<ResponseChain, DeleteINGPartyWithAncestors>() {
                        @Override
                        public DeleteINGPartyWithAncestors apply(ResponseChain responseChain) {
                            final Response response = responseChain.getOptionalResponse(0, "794");
                            if (response.getStatus() == ResponseStatus.NOT_FOUND) {
                                return null;
                            } else {
                                return deleteParty;
                            }
                        }
                    });
            return Function.destroyMyPerformance(responseFuture);
        } catch (MDMServiceException mdmec) {
            log.error("Exception Message : {}", mdmec);
            log.error("MDM Service Exception throwableMessage : {}", mdmec.getThrowableMessage());
            if (!StringUtils.isEmpty(mdmec.getThrowableMessage()) && mdmec.getThrowableMessage().contains("Party not found")) {
                throw new InvolvedException(ApplicationConstant.ERROR_CODE_0, ApplicationConstant.ERROR_PARTY_NOT_FOUND, Status.NOT_FOUND);
            } else {
                logAlertException(mdmec);
                throw new InvolvedException(ApplicationConstant.ERROR_CODE_0, ApplicationConstant.ERROR_CANNOT_EXECUTE_REQUEST, Status.INTERNAL_SERVER_ERROR);
            }
        }
    }


    private void logAlertException(MDMServiceException mdmec) {
        report.reportAlert(mdmec);
    }

    private InvolvedException handleMdmException(MDMServiceException mdmec) {
        log.error("Exception Message : ", mdmec);
        report.reportAlert(mdmec);
        return new InvolvedException(ApplicationConstant.ERROR_CODE_0, ApplicationConstant.ERROR_CANNOT_EXECUTE_REQUEST, Status.INTERNAL_SERVER_ERROR);
    }
}

interface MakeRequestChain {
    RequestChain requestChain();
}

class UpdateAddressRequest implements MakeRequestChain {
    private PartyAddress partyAddress;
    private String lastUpdateUser;
    private List<String> userRoles;

    public UpdateAddressRequest(PartyAddress partyAddress, String lastUpdateUser, List<String> userRoles) {
        this.partyAddress = partyAddress;
        this.lastUpdateUser = lastUpdateUser;
        this.userRoles = userRoles;
    }

    public RequestChain requestChain() {
        return partyAddress.toCommand().toChain("updatePartyAddress").withRole(userRoles).withRequesterName(lastUpdateUser);
    }

    public static java.util.function.Function<UpdateAddressRequest, RequestChain> makeRequestChain = (rc) -> rc.requestChain();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateAddressRequest that = (UpdateAddressRequest) o;
        return Objects.equals(partyAddress, that.partyAddress) &&
                Objects.equals(lastUpdateUser, that.lastUpdateUser) &&
                Objects.equals(userRoles, that.userRoles);
    }

    @Override
    public int hashCode() {

        return Objects.hash(partyAddress, lastUpdateUser, userRoles);
    }

}


class AbstractService<From, To> implements java.util.function.Function<From, To> {

    IErrorStrategy errorStrategy;
    Service<RequestChain, ResponseChain> mdmService;

    final java.util.function.Function<From, RequestChain> makeRequestChain;

    final java.util.function.Function<ResponseChain, To> fromResponseChain;

    public AbstractService(IErrorStrategy errorStrategy, Service<RequestChain, ResponseChain> mdmService, java.util.function.Function<From, RequestChain> makeRequestChain, java.util.function.Function<ResponseChain, To> fromResponseChain) {
        this.errorStrategy = errorStrategy;
        this.mdmService = mdmService;
        this.makeRequestChain = makeRequestChain;
        this.fromResponseChain = fromResponseChain;
    }

    Getter<To> resultGetter(From from) {return wrap(errorStrategy, map(mdmService.apply(makeRequestChain.apply(from)), fromResponseChain));}

    @Override
    public To apply(From from) {
        return resultGetter(from).get();
    }
}

class GetAddressRequest {
    String partyAddressIdPK;
    List<String> userRoles;

    public GetAddressRequest(String partyAddressIdPK, List<String> userRoles) {
        this.partyAddressIdPK = partyAddressIdPK;
        this.userRoles = userRoles;
    }

    RequestChain requestChain() {
        return PartyAddress.get(partyAddressIdPK).toChain("getPartyAddressByIdPK").withRole(userRoles);
    }

    static java.util.function.Function<GetAddressRequest, RequestChain> makeRequestChain = (r) -> r.requestChain();
}
