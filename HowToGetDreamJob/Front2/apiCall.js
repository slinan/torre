function openAlert() {
    swal({
        title: 'Select Outage Tier',
        input: 'select',
        inputOptions: {
            '1': 'Tier 1',
            '2': 'Tier 2',
            '3': 'Tier 3'
        },
        inputPlaceholder: 'required',
        showCancelButton: true,
        inputValidator: function (value) {
            return new Promise(function (resolve, reject) {
                if (value !== '') {
                    resolve();
                } else {
                    reject('You need to select a Tier');
                }
            });
        }
    }).then(function (result) {
        if (result.value) {
            swal({
                type: 'success',
                html: 'You selected: ' + result.value
            });
        }
    });
}

function getJobs() {
    select = document.getElementById('select');
    $.get("http://ec2-3-92-47-95.compute-1.amazonaws.com:8080/api/jobs", function (data) {
        data.forEach(element => {
            var opt = document.createElement('option');
            opt.value = element.dbId;
            opt.innerHTML = element.role;
            select.appendChild(opt)
        });
        document.getElementById("loader1").style.display = 'none';
        document.getElementById("mainDiv").style.display = 'inline';
    });
}

function getPeople() {
    select = document.getElementById('select');
    var selected = select.options[select.selectedIndex].value;
    document.getElementById("jobTitle").innerText = select.options[select.selectedIndex].innerText
    document.getElementById("paths").innerHTML = "";
    var people = document.getElementById("listPeople");
    people.innerHTML = ""
    if (selected == "none") {
        swal("Wait", "You must select a job", "error")
    } else {
        $.get("http://ec2-3-92-47-95.compute-1.amazonaws.com:8080/api/paths?dbId=" + selected, function (data) {
            data.forEach(person => {
                console.log(person)
                var li = document.createElement('li');
                li.classList.add("media");
                li.classList.add("my-4");
                var image = document.createElement('img');
                image.src = person.image
                image.width = 64
                image.classList.add("rounded-circle");
                image.classList.add("mr-3");
                var div = document.createElement('div');
                div.classList.add("media-body");
                var h5 = document.createElement('h5');
                h5.classList.add("mt-0");
                h5.classList.add("mb-1");
                h5.innerText = person.header
                var p = document.createElement('p');
                p.innerText = person.name

                li.append(image)
                li.append(div)
                div.append(h5)
                div.append(p)
                people.append(li)

                component = createPathComponent(person, select.options[select.selectedIndex].innerText);
                document.getElementById("paths").append(component)
            });
        });

        document.getElementById("part2").style.display = 'inline';

    }

}

function createPathComponent(person, jobName) {

    jobs = person.jobs.reverse()

    var div = document.createElement('div');
    div.classList.add("col-4");
    div.classList.add("list-group");

    for (let i = 1; i < jobs.length + 1; i++) {
        const job = jobs[i - 1];
        console.log(job)
        var partsOfStr = job.split(':');
        var a = document.createElement('a');
        a.classList.add("list-group-item");
        a.classList.add("list-group-item-action");
        if(partsOfStr.includes(jobName))
        {
            a.classList.add("active");
        }
        var div2 = document.createElement('div');
        div2.classList.add("d-flex");
        div2.classList.add("w-100");
        div2.classList.add("justify-content-between");
        var h5 = document.createElement('h5');
        h5.classList.add("mb-1");
        h5.innerText = i + ") " + partsOfStr[0]
        div2.classList.add("list-group");
        var p = document.createElement('p');
        p.classList.add("mb-1");
        if (partsOfStr[1]) {
            p.innerHTML = partsOfStr[1]
        }

        div.append(a)
        a.append(div2)
        div2.append(h5)
        a.append(p)
    }

    return div;


}


getJobs();