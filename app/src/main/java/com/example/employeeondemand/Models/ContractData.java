package com.example.employeeondemand.Models;

public class ContractData {
    String contractId, consumerId, serviceProviderId, serviceBudget, serviceTime, serviceWorkPlace, contractStatus;

    public ContractData(String contractId, String consumerId, String serviceProviderId, String serviceBudget, String serviceTime, String serviceWorkPlace, String contractStatus) {
        this.contractId = contractId;
        this.consumerId = consumerId;
        this.serviceProviderId = serviceProviderId;
        this.serviceBudget = serviceBudget;
        this.serviceTime = serviceTime;
        this.serviceWorkPlace = serviceWorkPlace;
        this.contractStatus = contractStatus;
    }

    public ContractData() {
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getServiceBudget() {
        return serviceBudget;
    }

    public void setServiceBudget(String serviceBudget) {
        this.serviceBudget = serviceBudget;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getServiceWorkPlace() {
        return serviceWorkPlace;
    }

    public void setServiceWorkPlace(String serviceWorkPlace) {
        this.serviceWorkPlace = serviceWorkPlace;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }
}
