$(document).ready(function() {

    $('.line').each(function() {
        var word = $(this);

        word.hover(function(e) {
          $(this).children('.link').show();
        }, function() {
            $(this).children('.link').hide();
        });
    });
});