from mpl_toolkits.mplot3d import Axes3D
import matplotlib.pyplot as plt
import numpy as np
import matplotlib.patches as mpatches

with open("results_lists_8_threads.csv", "rt") as f:
    data = f.read().splitlines()

data = list(map(lambda x: list(map(lambda y: float(y), x.split(","))), data))
x = list(map(lambda x: x[0], data))
y = list(map(lambda x: x[1], data))
time1 = list(map(lambda x: x[2], data))
time2 = list(map(lambda x: x[3], data))
sums = [sum(x) for x in zip(time1, time2)]
clr = ['#6e1710', '#fce803']
colors = [
    list(map(lambda tpl: clr[tpl[0] > tpl[1]], zip(time1, time2))),
    list(map(lambda tpl: clr[::-1][tpl[0] > tpl[1]], zip(time1, time2)))
]

print(x)
print(y)
print(time1)
print(time2)
print(sums)

DATA_LEN = len(x)
DATA_ROW_LEN = int(np.sqrt(DATA_LEN))

fig = plt.figure()
ax = fig.add_subplot(111, projection="3d")

ax.set_xlabel("inserting time (us)")
ax.set_ylabel("comparing time (us)")
ax.set_zlabel("time (ms)")
ax.set_xlim3d(-1, DATA_ROW_LEN*3)
ax.set_ylim3d(-1, DATA_ROW_LEN*3)

xpos = []
ypos = []
for x in range(DATA_ROW_LEN):
    for y in range(DATA_ROW_LEN):
        xpos.append(x*3)
        ypos.append(y*3)

zpos = np.zeros(DATA_LEN)
print(xpos)
print(ypos)

dx = np.ones(DATA_LEN)
dy = np.ones(DATA_LEN)
dz = [time1, time2]
_zpos = zpos


for i in range(2):
    ax.bar3d(xpos, ypos, _zpos, dx, dy, dz[i], color=colors[i], zsort='max', alpha=0.5)
    _zpos += dz[i]

ax.set_yticklabels(['', '10', '', '500', '', '1000'])
ax.set_xticklabels(['', '10', '', '500', '', '1000'])
ax.set_title("Wyniki symulacji\n2 wątki")


p1 = mpatches.Patch(color=clr[0], label='Fine-grained list')
p2 = mpatches.Patch(color=clr[1], label='Single lock list')
plt.legend(handles=[p1, p2], loc='upper left')

plt.gca().invert_xaxis()
plt.savefig('chart_results_lists_8_threads.png')
# plt.show()
