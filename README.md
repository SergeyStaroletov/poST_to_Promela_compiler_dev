# poST_to_Promela
Runnable with command "java -jar ./poST_to_promela.jar -i <input_poST_file> -o <output_Promela_file>".

Keys:
-i/--input - path to .post input file
-o/--output - path to .pml output file
-rt/--reduceTime - enables division of TIME constant values on their GCD
-lm/--ltlMacro - adds auxiliary macrodefinitions for ltl formulas to the end of the output file
