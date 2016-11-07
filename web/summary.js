$(document).ready(function() {

    $('.line').each(function() {
        var word = $(this);

        word.hover(function(e) {
          $(this).children('.linksblock').show();
        }, function() {
            $(this).children('.linksblock').hide();
        });
    });
});