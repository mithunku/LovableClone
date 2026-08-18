package com.codingShuttle.project.Lovable.llm.tools;

import com.codingShuttle.project.Lovable.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CodeGenerationTools {

    private final ProjectFileService projectFileService;
    private final Long projectId;

    //here we are passing file path and return file content
    //LLM WILL USE THIS MEHTOD WE ARE EXPOSING THIS METHOD TO LLM USING TOOL ANNOTATION
    @Tool(name = "read_files",description = "Read the content of files.Only input the file names present inside the FILE_TREE.Do not input any path which is not present under the FILE_TREE")
    public List<String> readFiles(@ToolParam(description = "List of relative paths (eg ['src/App.tsx]) dont add new or unknown file path use only the file path present in FILE_TREE") List<String> paths)
    {
        List<String> result=new ArrayList<>();

        for(String path: paths)
        {
            String cleanPath=path.startsWith("/")?path.substring(1): path;
            log.info("Requested to read file {}",cleanPath);
            String content=projectFileService.getFileContent(projectId,cleanPath).content();
            result.add(String.format("-----START OF FILE: %s ----\n%s\n----END OF FILE----",cleanPath,content));
        }

        return result;


    }


}
