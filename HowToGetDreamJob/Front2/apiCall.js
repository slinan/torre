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

                var mainDiv = document.createElement("div")
                mainDiv.classList.add("card");
                mainDiv.classList.add("border-info");
                mainDiv.classList.add("big-card");

                var body = document.createElement("div")
                body.classList.add("card-body");
                mainDiv.append(body);

                var media = document.createElement("div")
                media.classList.add("media");
                body.append(media)

                var img = document.createElement("img")
                img.src = person.image
                img.classList.add("mr-3");
                img.width = 64
                media.append(img)

                var mediaBody = document.createElement("div")
                mediaBody.classList.add("media-body");
                media.append(mediaBody)

                var heading = document.createElement("h5")
                heading.innerText = person.header
                heading.classList.add("mt-0");
                mediaBody.append(heading)

                var pName = document.createElement("p")
                pName.innerText = person.name
                mediaBody.append(pName)

                var cardDeck = document.createElement("div")
                cardDeck.classList.add("card-deck");
                body.append(cardDeck)

                jobs = person.jobs.reverse()

                for (let i = 1; i < jobs.length + 1; i++) {
                    const job = jobs[i - 1];
                    var partsOfStr = job.split(':');

                    var jobCard = document.createElement("div")
                    jobCard.classList.add("card")
                    jobCard.classList.add("mb-4")
                    if (partsOfStr.includes(select.options[select.selectedIndex].innerText)) {
                        jobCard.classList.add("bg-success")
                        jobCard.classList.add("text-white")
                    }
                    cardDeck.append(jobCard)

                    var cardHeader = document.createElement("div")
                    cardHeader.innerText = i
                    cardHeader.classList.add("card-header")
                    jobCard.append(cardHeader)

                    var cardBody = document.createElement("div")
                    cardBody.classList.add("card-body")
                    jobCard.append(cardBody)

                    var jobName = document.createElement("h5")
                    jobName.classList.add("card-title")
                    jobName.innerText = partsOfStr[0]
                    cardBody.append(jobName)

                    if (partsOfStr[1]) {
                        var text = document.createElement("p")
                        text.classList.add("card-text")
                        text.innerText=partsOfStr[1]
                        cardBody.append(text)
                    }
                }
                document.getElementById("paths").append(mainDiv);

            });
        });
        document.getElementById("part2").style.display = 'inline';

    }

}

getJobs();