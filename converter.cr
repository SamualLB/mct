require "csv"
require "json"
 
csv = <<-CSV
    "How many children does a binary tree have?","2","Any number of children","0 or 1 or 2","0 or 1","C"
    "What is/are the disadvantages of implementing tree using normal arrays?","Difficulty in knowing children nodes of a node","Difficulty in finding the parent of a node","Have to know the maximum number of nodes possible before creation of trees","difficult to implement","C"
    "What is a hash table?","A structure that maps values to keys","A structure that maps keys to values","A structure used for storage","A structure used to implement stacks and queues","B"
    "What does ‘stack underflow’ refer to?","Accessing item from an undefined stack","Adding items to a full stack","Removing items from an empty stack","Index out of bounds exception","D"
    "What is a bit array?","Data structure for representing arrays of records","Data structure that compactly stores bits","An array in which most of the elements have the same value","Array in which elements are not present in continuous locations","B"
    "Why is implementation of stack operations on queues not feasible for a large dataset (Assume the number of elements in the stack to be n)?","Because of its time complexity O(n)","Because of its time complexity O(log(n))","Extra memory is not required","There are no problems","A"
    "Which of the following statements for a simple graph is correct?","Every path is a trail","Every trail is a path","Every trail is a path as well as every path is a trail","Path and trail have no relation","A"
    "The number of elements in the adjacency matrix of a graph having 7 vertices is...","7","14","36","49","D"
    "Which of these adjacency matrices represents a simple graph?","[ [1, 0, 0], [0, 1, 0], [0, 1, 1] ]","[ [1, 1, 1], [1, 1, 1], [1, 1, 1]","[ [0, 0, 1], [0, 0, 0], [0, 0, 1] ]","[ [0, 0, 1], [1, 0, 1], [1, 0, 0] ]","D"
    "Express -15 as a 6-bit signed binary number","001111","101111","101110","001110","B"
    CSV
out_string = JSON.build(2) do |json|
  json.object do
  	json.field "time_limit", 600
    json.field "no_questions", 5
    json.field "questions", do
      json.array do
        CSV.each_row(csv) do |row|
          json.object do
            json.field "question", row[0]
            json.field "a", row[1]
            json.field "b", row[2]
            json.field "c", row[3]
            json.field "d", row[4]
            json.field "answer", row[5]
          end
        end
      end
    end
  end
end
 
puts out_string