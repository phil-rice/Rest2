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
import static org.invertthepyramid.involved.Wrap.*;

public class InvolvedImpl implements Involved {
    @Value("${Alert.alert.dtapstage}")
    private String AlertDtapStage;
    @Autowired
    Service<RequestChain, ResponseChain> mdmService;

    @Autowired
    AlertReporter report;

    private static LoggerAdapter log = null;//would normally be Logger.get... etc




    @Override
    public PartyAddress updateAddress(PartyAddress partyAddress, String lastUpdateUser, List<String> userRoles) {
        return wrap(IErrorStrategy.checkConnection(log, report), map(mdmService.apply(
                partyAddress.toCommand().toChain("updatePartyAddress").withRole(userRoles).withRequesterName(lastUpdateUser)),
                (responseChain) -> PartyAddress.fromResponse(responseChain.getSafeResponse(0).getObject(0))
        ));
    }

    private InvolvedException handleMdmException(MDMServiceException mdmec) {
        log.error("Exception Message : ", mdmec);
        report.reportAlert(mdmec);
        return new InvolvedException(ApplicationConstant.ERROR_CODE_0, ApplicationConstant.ERROR_CANNOT_EXECUTE_REQUEST, Status.INTERNAL_SERVER_ERROR);
    }

    @Override
    public PartyAddress getAddress(String partyAddressIdPK, List<String> userRoles) {
        try {
            Future<PartyAddress> responseFutureSecondCall = null;
            RequestChain requestChaingetAllPartyAdminSysKeys = PartyAddress
                    .get(partyAddressIdPK).toChain("getPartyAddressByIdPK").withRole(userRoles);
            responseFutureSecondCall = mdmService.apply(requestChaingetAllPartyAdminSysKeys).map(new AbstractFunction1<ResponseChain, PartyAddress>() {
                @Override
                public PartyAddress apply(ResponseChain responseChain) {
                    final Response response = responseChain.getOptionalResponse(0, "893");
                    if (response.getStatus() == ResponseStatus.SUCCESS) {
                        return PartyAddress.fromResponse(response.getObject(0), true);
                    } else {
                        return null;
                    }
                }
            });


            return Function.destroyMyPerformance(responseFutureSecondCall);

        } catch (MDMServiceException mdmec) {
            throw handleMdmException(mdmec);
        }
    }

    @Override
    public ContactMethod getContactMethod(String partyContactMethodIdPK, boolean keepObjectAlive, List<
            String> userRoles) {
        try {
            Future<ContactMethod> responseFuture = null;
            final GetPartyContactMethodByIdPK getPartyContactMethodByIdPK = ContactMethod.getPartyContactMethodByIdPK(partyContactMethodIdPK);
            final RequestChain requestChain = getPartyContactMethodByIdPK.toChain("getPartyContactMethodByIdPK").withRole(userRoles);

            responseFuture = mdmService.apply(requestChain)
                    .map(new AbstractFunction1<ResponseChain, ContactMethod>() {
                        @Override
                        public ContactMethod apply(ResponseChain responseChain) {
                            final Response response = responseChain.getOptionalResponse(0, "893");
                            if (response.getStatus() == ResponseStatus.SUCCESS) {
                                return ContactMethod.fromResponse(response.getObject(0), keepObjectAlive);
                            } else {
                                return null;
                            }
                        }
                    });

            return Function.destroyMyPerformance(responseFuture);

        } catch (MDMServiceException mdmec) {
            throw handleMdmException(mdmec);
        }
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

}
