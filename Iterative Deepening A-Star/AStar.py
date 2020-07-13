#Credit to Sarit and Professot Piotr for base code

import random
import math
import time
import psutil
import os
from collections import deque
from heapq import *
from __builtin__ import str
from re import search

CUTOFF = -1

#This class defines the state of the problem in terms of board configuration
# This class contains the board size and a method to execute an action and returning  a board based off of that
class Board:
    def __init__(self,tiles):
        self.size = int(math.sqrt(len(tiles))) # defining length/width of the board
        self.tiles = tiles
    
    #This function returns the resulting state from taking particular action from current state
    # Meaning that it takes in an action and then sees whether it is a valid one. If it is, then it does it
    def execute_action(self,action):
        new_tiles = self.tiles[:]
        empty_index = new_tiles.index('0')
        if action=='l':
            if empty_index%self.size>0:
                new_tiles[empty_index-1],new_tiles[empty_index] = new_tiles[empty_index],new_tiles[empty_index-1]
        if action=='r':
            if empty_index%self.size<(self.size-1):
                new_tiles[empty_index+1],new_tiles[empty_index] = new_tiles[empty_index],new_tiles[empty_index+1]
        if action=='u':
            if empty_index-self.size>=0:
                new_tiles[empty_index-self.size],new_tiles[empty_index] = new_tiles[empty_index],new_tiles[empty_index-self.size]
        if action=='d':
            if empty_index+self.size < self.size*self.size:
                new_tiles[empty_index+self.size],new_tiles[empty_index] = new_tiles[empty_index],new_tiles[empty_index+self.size]
        return Board(new_tiles) # return new board.
        

#This class defines the node on the search tree, consisting of state, parent and previous action
class Node:
    
    def __init__(self,state,parent,action):
        self.state = state
        self.parent = parent
        self.action = action
        
        if parent is None:
            self.cost=0
        else:             # If they DO have a parent, then keep track of the cost (aka depth)
            self.cost = parent.cost+1
    
    #Returns string representation of the state
    def __repr__(self):
        return str("state: " +str(self.state.tiles) + "prev_action "+ str(self.action))
    
    #Comparing current node with other node. They are equal if states are equal
    def __eq__(self,other):
        return self.state.tiles == other.state.tiles

# Inner class which solves the problem using the specific  heursitic mentioned
class Astar:
    def __init__(self,start,goal,heuristic):
        self.startState = start
        self.goal_state = goal
        self.heuristic = heuristic
        self.node_expanded = 0
        

    # This function and the one below are the two main ones that do the computing
    def aStarIter(self, expanded):
        limit = self.f_value(self.startState) # set the limit to be f(n) of our root
        infi = 1
        while infi == 1: #infinitely keep on searching
            expanded = expanded+ 1
            temp = self.search(self.startState, limit) #ALWAYS start from the root node!
            if(isinstance(temp, Node)): # if temp is an instance of class Node we know we've found our goal
                find_path(temp)
                return find_path(temp), expanded
            limit = temp
        
        return 1
    
    # Recursively search through children until limit is found
    def search(self, curNode, limit):
        fVal = self.f_value(curNode)
        self.node_expanded = self.node_expanded + 1
        if(self.goal_test(curNode.state.tiles)):
            return curNode
        if(fVal > limit):
            return fVal
        minNum = 1000000000 # big number
        
        # explore the children
        for child in get_children(curNode):
            temp = self.search(child, limit)
            if(isinstance(temp, Node)):
                return temp
            
            if(temp < minNum):
                minNum  = temp
                
        #Return the smallest f(n) that is greater than our currrent limit, since we want as little work as possible
        return minNum
        
    def f_value(self,node):
        # cost to get to this node + specified heuristic
        return node.cost + self.h_value(node)
        
    # gets the specific heuristic (Mahattan or misplaced tiles)
    def h_value(self,node):
        if self.heuristic=="manhattan":
            return self.manhattan_heuristic(node)
        else:
            return self.misplaced_tiles_heuristic(node)
    
    #This function calculates sum of Manhattan distances of each tile from goal position
    def manhattan_heuristic(self,node):
        tiles = node.state.tiles
        size = node.state.size
        total_sum_distances = 0
        for i in range(0,len(tiles)):
            value = int(tiles[i])
            if value==0 : continue
            current_x = i / size
            current_y = i % size
            correct_x = (value-1) / size
            correct_y = (value-1) % size
            
            cur_distance = abs(correct_x-current_x) + abs(correct_y-current_y)
            total_sum_distances += cur_distance
        return total_sum_distances
    
    #This function calculates number of misplaced tiles from goal position
    def misplaced_tiles_heuristic(self,node):
        tiles = node.state.tiles
        num_misplaced = 0
        for i in range(1,len(tiles)):
            if i!=int(tiles[i-1]) : num_misplaced+=1
            
        return num_misplaced
        
    # Used to check if we have arrived at our goal state
    def goal_test(self,cur_state):
        return cur_state == self.goal_state

    
        
#Utility function to randomly generate 15-puzzle
def generate_puzzle(size):
    numbers = list(range(size*size))
    random.shuffle(numbers)
    return Node(Board(numbers),None,None)

#This function returns the list of children obtained after simulating the actions on current node
def get_children(parent_node):
    children = []
    actions = ['l','r','u','d'] # left,right, up , down ; actions define direction of movement of empty tile
    for action in actions:
        child_state = parent_node.state.execute_action(action)
        child_node = Node(child_state,parent_node,action)
        children.append(child_node)
    return children

#This function backtracks from current node to reach initial configuration. The list of actions would constitute a solution path
def find_path(node):
    path = []
    while(node.parent is not None):
        path.append(node.action)
        node = node.parent
    path.reverse()
    return path



def main():
    process = psutil.Process(os.getpid())
    initial_memory = process.memory_info().rss / 1024.0
    initial_time = time.time()
    initial = str(raw_input("initial configuration: ")) # take in input from user
    initial_list = initial.split(" ")
    root = Node(Board(initial_list),None,None) # create a Node Object (i think) based on the given input, obvs has no parent or prev. action
    goal_state = ['1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','0']
    astar = Astar(root, goal_state,"manhattan")
    print("manhattan_heuristic: "+ str(astar.manhattan_heuristic(root)))

    x = 0
    yuh = astar.aStarIter(x)
    final_time = time.time()
    print "The path: "+str(yuh[0])
    print "The number of iterations: " + str(yuh[1])
    print "The nodes expanded: " +   str( astar.node_expanded)
   
    final_memory = process.memory_info().rss / 1024.0
    print("time taken: "+str(final_time-initial_time))
    print(str(final_memory-initial_memory)+" KB")

    astar = Astar(root, goal_state,"misplaced")
    print("Misplaced Tiles: " + str(astar.misplaced_tiles_heuristic(root)))
    x= 0
    k = astar.aStarIter(x)
    final_time = time.time()
    print("path: "+ str(k[0]))
    print("Number of iterations :" + str(k[1]))
    print("Nodes expanded: " + str(astar.node_expanded))
    final_memory = process.memory_info().rss / 1024.0
    print("time taken: "+str(final_time-initial_time))
    print(str(final_memory-initial_memory)+" KB")
    
if __name__=="__main__":
    main()
