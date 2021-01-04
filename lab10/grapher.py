import numpy as np
import matplotlib.pyplot as plt
import network_line_graph as nlg
from collections import defaultdict
from PIL import Image
import networkx as nx


class Graph:
    def __init__(self, word, independent_operations):
        self.edges = defaultdict(list)
        self.word = word
        self.N = len(word)
        self.independent_operations = independent_operations

        self.build_edges()

    def build_edges(self):
        for pair in self.independent_operations.copy():
            self.independent_operations.add((pair[1], pair[0]))

        for i, l in enumerate(self.word[:-1]):
            edges_found = list(filter(lambda x: x[0] == l, list(self.independent_operations)))
            for j, l2 in enumerate(self.word[i + 1:], i + 1):
                if not any([l2 == e for _, e in edges_found]):
                    self.edges[i].append(j)

    def remove_edges(self, to_remove):
        for a, b in to_remove:
            self.edges[a].remove(b)

    def build_one_stage(self, letter_idx, d):
        a1 = np.zeros((self.N, self.N), dtype=bool)
        for e in self.edges[letter_idx]:
            a1[letter_idx, e] = True

        w1 = np.full((self.N, self.N), 0.6)
        w1[~a1] = np.nan

        if not np.all(np.isnan(w1)):
            nlg.draw(w1, arc_above=d, node_labels={i: k for i, k in enumerate(self.word)}, node_order=np.array(range(self.N)))


    def draw(self, dirs=None):
        if not dirs:
            dirs = [(v + 1) % 2 for v in range(self.N - 1)]

        for i, d in enumerate(dirs):
            self.build_one_stage(i, d)

    def remove_transitive_edges(self):
        to_remove = set()
        for i in range(self.N):
            for j in range(self.N):
                for k in range(self.N):
                    if j in self.edges[i] and k in self.edges[j] and k in self.edges[i]:
                        to_remove.add((i, k))

        self.remove_edges(list(to_remove))

    def save_graph(self, fname):
        self.draw()
        self.save_graph_and_crop(fname, [-2, 2])


    def save_graph_and_crop(self, fname, lims=None):
        fname = "{}.png".format(fname)
        ax = plt.gca()
        ax.set_ylim(lims)
        plt.savefig(fname, bbox_inches='tight')
        plt.clf()

        self.crop_white_space(fname)

    def crop_white_space(self, fname):
        im = Image.open(fname)
        pix = np.asarray(im)
        pix = pix[:, :, 0:3]
        idx = np.where(pix - 255)[0:2]
        box = list(map(min, idx))[::-1] + list(map(max, idx))[::-1]
        region_pix = np.asarray(im.crop(box))
        im = Image.fromarray(region_pix)
        im.save(fname)

    def save_pretty_graph(self, fname, layout):
        G = nx.DiGraph()
        for i in range(self.N):
            for j in self.edges[i]:
                G.add_edge(i, j)

        labels = {k: v for k, v in zip(range(self.N), self.word)}

        nx.draw_networkx_nodes(G, layout, node_color='white', node_size=600, edgecolors='black')
        nx.draw_networkx_labels(G, layout, font_size=16, font_color="black", font_weight='bold', labels=labels)
        nx.draw_networkx_edges(G, layout, width=2, arrowsize=20)
        plt.axis('off')

        self.save_graph_and_crop(fname + "_reduced_pretty")


def generate_graph(word, independent_operations, fname):
    graph = Graph(word, independent_operations)
    graph.save_graph(fname)

    graph.remove_transitive_edges()
    graph.save_graph(fname + "_reduced")

    return graph


def main():
    word = 'baadcb'
    i1 = {('a', 'd'), ('b', 'c')}
    G = generate_graph(word, i1, "graph1")
    G.save_pretty_graph("graph1", {0: [-0.2, 3], 1: [-0.3, 2.5], 2: [-0.3, 2], 3: [-0.1, 2.5], 4: [-0.2, 2.2], 5: [-0.1, 2]})


    word = 'acdcfbbe'
    i2 = {('a', 'd'), ('b', 'e'), ('c', 'd'), ('c', 'f')}
    G = generate_graph(word, i2, "graph2")
    G.save_pretty_graph("graph2", {0: [-0.2, 3], 1: [-0.3, 2.5], 2: [-0.1, 3], 3: [-0.3, 2], 4: [-0.1, 2.5], 5: [-0.2, 2.3], 6: [-0.2, 2.0], 7: [-0.2, 1.6]})


if __name__ == '__main__':
    main()
