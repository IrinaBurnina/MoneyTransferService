package ir.bu.moneytransferservice.service;

//import ir.bu.moneytransferservice.model.Operation;
//import ir.bu.moneytransferservice.model.dto.ConfirmOperationDTO;
//import ir.bu.moneytransferservice.model.dto.OperationDTO;
//import ir.bu.moneytransferservice.model.dto.OperationDtoResponses;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//@Mapper(componentModel = "spring")//TODO методы сами по себе трапсформируют объекты , без маппера
//public interface MoneyTransferServiceDTOTransferMapper {
//    @Mapping(target = "id", source = "operation.id")
//    @Mapping(target = "sender", source = "operation.sender")
//    @Mapping(target = "recipientCardNumber", source = "operation.recipientCardNumber")
//    @Mapping(target = "amount", source = "operation.amount")
//    @Mapping(target = "fee", source = "operation.fee")
//    @Mapping(target = "operationStatus", source = "operation.operationStatus")
//    OperationDTO OperationToOperationDTO(Operation operation);
//
//    @Mapping(target = "id", source = "operationDTO.id")
//    @Mapping(target = "sender", source = "operationDTO.sender")
//    @Mapping(target = "recipientCardNumber", source = "operationDTO.recipientCardNumber")
//    @Mapping(target = "amount", source = "operationDTO.amount")
//    @Mapping(target = "fee", source = "operationDTO.fee")
//    @Mapping(target = "operationStatus", source = "operationDTO.operationStatus")
//    Operation OperationDtoToOperation(OperationDTO operationDTO);
//    @Mapping(target = "id", source = "confirmOperationDTO.id")
//    @Mapping(target = "sender", source = "confirmOperationDTO.sender")
//    @Mapping(target = "recipientCardNumber", source = "confirmOperationDTO.recipientCardNumber")
//    @Mapping(target = "amount", source = "confirmOperationDTO.amount")
//    @Mapping(target = "fee", source = "confirmOperationDTO.fee")
//    @Mapping(target = "operationStatus", source = "confirmOperationDTO.operationStatus")
//    OperationDTO ConfirmOperationDtoToOperationDTO(ConfirmOperationDTO confirmOperationDTO);


//    ConfirmOperationDTO OperationDtoToConfirmOperation(OperationDTO operationDTO);
//
//    ConfirmOperationDTO ConfirmOperationToConfirmOperationDTO(ConfirmOperation confirmOperation);
//
//    ConfirmOperation ConfirmOperationDTOToConfirmOperation(ConfirmOperationDTO confirmOperationDTO);
//
//    ConfirmOperationDTO OperationDtoResponsesToConfirmOperationDTO(OperationDtoResponses operationDtoResponses);
//
//    OperationDtoResponses ConfirmOperationDtoToOperationDtoResponses(ConfirmOperationDTO confirmOperationDTO);
//    @Mapping(target = "id", source = "operation.id")
//    OperationDtoResponses OperationToOperationDtoResponses(Operation operation);
//    @Mapping(target = "id", source = "operationDTO.id")
//
//    OperationDtoResponses OperationDtoToOperationDtoResponses(OperationDTO operationDTO);
//}

//    MoneyTransServRepositoryImpl repository = null;
//    Operation operationDTOOperation(OperationDTO operationDTO);
//    void confirm(OperationDTO operationDTO);
//
//    void transfer(OperationDTO operationDTO);

//



