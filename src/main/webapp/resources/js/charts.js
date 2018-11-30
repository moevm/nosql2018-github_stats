function drawCircleDiagram(data) {
    let colors = data.dataset.map(getRandomColor);
    new Chart($(SELECTOR.CANVAS_CIRCLE)[0].getContext("2d"), {
        type: 'doughnut',
        data: {
            datasets: [{
                data: data.dataset,
                backgroundColor: colors.map(addAlphaToColor),
                borderColor: colors,
                borderWidth: 1
            }],

            labels: data.labels
        }
    });
}

function drawTopDiagram(data) {
    let colors = data.dataset.map(getRandomColor);
    new Chart($(SELECTOR.CANVAS_TOP)[0].getContext("2d"), {
        type: 'bar',
        data: {
            datasets: [{
                data: data.dataset,
                backgroundColor: colors.map(addAlphaToColor),
                borderColor: colors,
                borderWidth: 1
            }],
            labels: data.labels
        }
    });
}

function drawTimeline(data) {
    let p = {
        type: 'line',
        data: {
            datasets: data.dataset.map(getDataObject),
        },
        options: {
            scales: {
                xAxes: [{
                    type: 'time',
                    distribution: 'series'
                }],
                yAxes : [{
                    ticks : {
                        min : 0
                    }
                }]
            }
        }
    };
    new Chart($(SELECTOR.CANVAS_TIMELINE)[0].getContext("2d"), p);
}

function getRandomColor() {
    return `rgba(${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, 1)`
}

function addAlphaToColor(color) {
    return `${color.slice(0, -2)}0.3)`
}

function getPoint(point) {
    let data = {};
    data.t = new Date(point.x);
    data.y = point.y;
    return data;
}

function getDataObject(chartInfo) {
    let data = {};
    data.data = chartInfo.points.map(getPoint);
    data.label = chartInfo.label;
    data.backgroundColor = addAlphaToColor(getRandomColor());
    return data;
}