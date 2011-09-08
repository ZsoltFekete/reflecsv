#!/bin/bash -eu

echo Starting acceptance tests

TEST_OUT=test_data/test_out.txt
java -cp build hu.sztaki.ilab.reflecsv.example.Example test_data/test_file.csv 1>"$TEST_OUT"
diff test_data/expected_test_result.txt "$TEST_OUT"
rm "$TEST_OUT"

TEST_OUT=test_data/test_out.txt
java -cp build hu.sztaki.ilab.reflecsv.example.ReadByFileNameExample test_data/test_file.csv 1>"$TEST_OUT"
diff test_data/expected_test_result.txt "$TEST_OUT"
rm "$TEST_OUT"

TEST_OUT=test_data/test_out.txt
java -cp build hu.sztaki.ilab.reflecsv.example.RecordReaderByFileNameExample test_data/test_file.csv 1>"$TEST_OUT"
diff test_data/expected_test_result.txt "$TEST_OUT"
rm "$TEST_OUT"

TEST_OUT=test_data/test_out.txt
java -cp build hu.sztaki.ilab.reflecsv.example.RecordMapReaderByFileNameExample test_data/test_file.csv 1>"$TEST_OUT"
diff test_data/expected_test_result2.txt "$TEST_OUT"
rm "$TEST_OUT"

echo Finished

exit

