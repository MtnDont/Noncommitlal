from collections import defaultdict

def depth_first_search(graph, starting_vertex):
    visited = set()
    counter = [0]
    traversal_times = defaultdict(dict)

    def traverse(vertex):
        visited.add(vertex)
        counter[0] += 1
        traversal_times[vertex]['discovery'] = counter[0]

        for next_vertex in graph[vertex]:
            if next_vertex not in visited:
                traverse(next_vertex)

        counter[0] += 1
        traversal_times[vertex]['finish'] = counter[0]

    # in this case start with just one vertex, but we could equally
    # dfs from all_vertices to produce a dfs forest
    traverse(starting_vertex)
    return traversal_times

if __name__ == '__main__':
    #simple_graph = {
    #    'A': ['B', 'D'],
    #    'B': ['C', 'D'],
    #    'C': [],
    #    'D': ['E'],
    #    'E': ['B', 'F'],
    #    'F': ['C']
    #}

    n = int(input("Enter num of nodes: "))
    sep = input("Enter separator in the adj list: ")
    g = {}

    for j in range(n):
        nodeIn = input("Enter adj string(node[sep]node[sep]...node): ")
        arr = nodeIn.split(sep)
        garr = []
        #print(arr)
        for i in range(1, len(arr)):
            garr.append(arr[i])
        g[str(j+1)] = garr
    
    #print(g)
    traversal_times = depth_first_search(g, '1')
    #print(traversal_times)
    for k in traversal_times:
        print(k, end='')
        for v in traversal_times[k]:
            print(" " + v + ": " + str(traversal_times[k][v]), end='')
        print()