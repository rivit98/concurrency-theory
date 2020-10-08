import numpy as np
from numpy.random import default_rng
from scipy.special import gammaincc
import random
import functools
import matplotlib as mtplt
from scipy.stats import norm, shapiro
from matplotlib import pyplot as plt

from collections import Counter
mtplt.rcParams['figure.figsize'] = [16, 7] # plots size

def generate_histogram():
    with open("./hist1.txt", "rt") as f:
        data = f.read()

    nums = list(map(lambda x: int(x), data.strip().split(",")))

    c = Counter(nums)

    data = list(zip(list(c.keys()), list(c.values())))
    data = sorted(data, key=lambda x: x[0])
    x, y = [list(t) for t in zip(*data)]
    values_num = len(x)
    labels = list(map(lambda xx: str(xx), x))

    indexes = range(values_num)
    bars = plt.bar(indexes, height=y)
    plt.xticks(indexes, labels)

    for bar in bars:
        yval = bar.get_height()
        plt.text(bar.get_x() + 0.2, yval + .5, yval)

    # plt.show()
    plt.savefig("hist1.png")


if __name__ == "__main__":
    generate_histogram()
