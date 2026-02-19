package com.sunny.controller;

import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.dto.ChildDto;
import com.sunny.model.dto.ParentsDto;
import com.sunny.model.embedded.Address;
import com.sunny.service.ExcelChildService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelController extends BasicController {

    private final ExcelChildService excelChildService;

    @PostMapping("/child")
    public ResponseEntity<Map<String, Object>> addChildAsExcel(@RequestParam("file") MultipartFile file) {
        List<ChildDto> childDtoList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            ChildDto childDto = null;
            List<ParentsDto> parentsDtoList = new ArrayList<>();

            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                
                //원아정보 매핑
                if (row.getCell(0) != null) {
                    childDto = ChildDto.builder()
                            .name(getCellValueAsString(row.getCell(0)))
                            .status(getCellValueAsString(row.getCell(1)))
                            .className(getCellValueAsString(row.getCell(2)))
                            .admissionDate(LocalDate.parse(getCellValueAsString(row.getCell(3))))
                            .birthday(LocalDate.parse(getCellValueAsString(row.getCell(4))))
                            .address(Address.builder()
                                    .zipCode(getCellValueAsString(row.getCell(5)))
                                    .address(getCellValueAsString(row.getCell(6)))
                                    .detailAddress(getCellValueAsString(row.getCell(7)))
                                    .build())
                            .build();


                }
                //부모정보 매핑
                if (row.getCell(8) != null) {
                    ParentsDto parentsDto = null;
                    parentsDto = ParentsDto.builder()
                            .name(getCellValueAsString(row.getCell(8)))
                            .relation(getCellValueAsString(row.getCell(9)))
                            .telephone(getCellValueAsString(row.getCell(10)))
                            .build();

                    parentsDtoList.add(parentsDto);
                }

                //다음행에 원아정보는 없고 부모정보만 있다면 원아 동료 리스트에 합하지 않고 다시 실행
                if(sheet.getRow(i+1).getCell(0) == null && sheet.getRow(i+1).getCell(8) != null) {
                    continue;
                }

                if(parentsDtoList.size() != 0) {
                    childDto.setParentList(parentsDtoList);
                    parentsDtoList = new ArrayList<>();
                }

                childDtoList.add(childDto);
            }
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILEIOEXCEPTION);
        }
        if(childDtoList.size() == 0) throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "엑셀에 기입된 내용에 오류가 없는지 확인해주세요.");

        return createResponse(excelChildService.createList(childDtoList));
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
