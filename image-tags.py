import ollama
import os
from pydantic import BaseModel
from pathlib import Path
from typing import List, Dict

class ImageTags(BaseModel):
    tags: List[str]

def process_image(image_path: Path) -> Dict:
    try:
        #print(image_path)
        # Ensure image path exists
        #if not image_path.exists():
        #    raise FileNotFoundError(f"Image not found: {image_path}")

        # Convert image path to string for Ollama
        image_path_str = str(image_path)
        print(image_path + " - " +image_path_str)

        # Get tags
        tags_response = get_tags(image_path_str)
        #print(tags_response)
        print(image_path_str + " - " +tags_response)
       
        return {
            "tags": tags_response.tags
        }

    except Exception as e:
        raise

def get_tags(image_path: str) -> ImageTags:
    """Get structured tags for the image."""
    response = query_ollama(
        "List 5-10 relevant tags for this image. Include both objects, artistic style, type of image, color, etc.",
        image_path,
        ImageTags.model_json_schema()
    )
    return ImageTags.model_validate_json(response)

def query_ollama(prompt: str, image_path: str, format_schema: dict) -> str:
    """Send a query to Ollama with an image for structured output."""
    try:
        response = ollama.chat(
            model='llama3.2-vision',
            messages=[{
                'role': 'user',
                'content': prompt,
                'images': [image_path],
                'options': {
                    'num_gpu': 41
                }
            }],
            format=format_schema
        )
        return response['message']['content']
    except Exception as e:
        raise

def process_all_images_from_folder(folder: str):
    files = os.listdir(folder)
    # Filtering only the files.
    files = [f for f in files if os.path.isfile(folder+'/'+f)]
    #print(*files, sep="\n")
    for image in files:
        process_image(folder+'/'+image)

process_all_images_from_folder("C:/Users/Anand Kulkarni/Desktop/images")
#process_image("C:/Users/Anand Kulkarni/Desktop/images/n_1.jpg")
