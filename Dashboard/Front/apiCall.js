function updateGraph() {
    $.get("http://ec2-54-175-21-223.compute-1.amazonaws.com:5000/lastReadings", function (data) {
        var array = JSON.parse(data);
        console.log(array)
        total = 0;
        lastReading = array[array.length - 1]
        valueLastReading = calculateAverage(lastReading).toFixed(2)
        average12 = 0
        average24 = 0
        number = 0
        total = 0
        dataS1 = []
        dataS2 = []
        dataS3 = []
        dataS4 = []
        labels = []
        for (let i = array.length - 1; i >= 0; i--) {
            const element = array[i];
            number = number + 1
            total = total + calculateAverage(element)
            dataS1.push(element.s1)
            dataS2.push(element.s2)
            dataS3.push(element.s3)
            dataS4.push(element.s4)
            labels.push(element.hour + ':' + element.minute)

            if (number == 12) {
                average12 = total / 12
                average12 = average12.toFixed(2)
            }

            if (number == 24) {
                average24 = total / 24
                average24 = average24.toFixed(2)
            }

            console.log('total', total)
            console.log(number)
        }

        removeData()
        datasets = [{
            label: "Virginia",
            backgroundColor: color1,
            borderColor: color1,
            fill: false,
            data: dataS1.reverse(),
        }, {
            label: "California",
            backgroundColor: color2,
            borderColor: color2,
            fill: false,
            data: dataS2.reverse(),
        }, {
            label: "London",
            backgroundColor: color3,
            borderColor: color3,
            fill: false,
            data: dataS3.reverse(),
        }, {
            label: "Tokio",
            backgroundColor: color4,
            borderColor: color4,
            fill: false,
            data: dataS4.reverse(),
        }]

        addData(labels.reverse(), datasets.reverse());


        document.getElementById('lastField').innerText = valueLastReading
        document.getElementById('lastSix').innerText = average12
        document.getElementById('lastTwelve').innerText = average24
    });
}

function calculateAverage(reading) {
    s1 = reading.s1
    s2 = reading.s2
    s3 = reading.s3
    s4 = reading.s4
    elementAverage = (s1 + s2 + s3 + s4) / 4
    return elementAverage

}

window.setInterval(function () {
    updateGraph()
}, 60000);
updateGraph()