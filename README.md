# poST to Promela transpiler

Runnable with command "java -jar ./poST_to_promela.jar -i <input_poST_file> -o <output_Promela_file>".<br/>
<br/>
Keys:<br/>
-i/--input - path to .post input file,<br/>
-o/--output - path to .pml output file,<br/>
-rt/--reduceTime - enables division of TIME constant values on their GCD,<br/>
-lm/--ltlMacro - adds auxiliary macrodefinitions for ltl formulas to the end of the output file.


How to build from sources: 
1. Install Eclipse for DSL developers: https://www.eclipse.org/downloads/packages/release/2022-12/r/eclipse-ide-java-and-dsl-developers
2. In a new workspace, import "iae.post" and "iae.post.generator.promela" projects (File->New->Existing projects into Workspace)
3. In the "iae.post" project, in src/iae.post on the file GeneratePoST.nwe2 click "Run as" and execute "MWE2 workflow" to create Ecore models. 
4. The project "iae.post" is just a referenced project in this solution. The project "iae.post.generator.promela" is runnable. 
5. To run and debug, create a new run configuration and specify "iae.post.generator.promela.Main" as the main class. Put needed arguments in the "arguments" tab. Sample system in poST is in "Samples".
